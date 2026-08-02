package project.llmevaluation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import project.llmevaluation.data.HistoryEntity
import project.llmevaluation.models.EvaluationResult
import project.llmevaluation.models.LLMRequest
import project.llmevaluation.models.Message
import project.llmevaluation.python.PythonBridge
import project.llmevaluation.repository.LLMRepository

class LLMViewModel(
    private val repository: LLMRepository,
    private val pythonBridge: PythonBridge
) : ViewModel() {

    private val _evaluationResults = MutableLiveData<List<EvaluationResult>>()
    val evaluationResults: LiveData<List<EvaluationResult>> = _evaluationResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun compareModels(prompt: String, selectedModels: List<String>, apiKey: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val deferredResponses = selectedModels.map { model ->
                    async {
                        val request = LLMRequest(
                            model = model,
                            messages = listOf(Message(role = "user", content = prompt))
                        )
                        val response = repository.getCompletion(apiKey, request)
                        if (response.isSuccessful) {
                            val text = response.body()?.choices?.firstOrNull()?.message?.content ?: ""
                            mapOf("model" to model, "text" to text, "reference" to prompt)
                        } else {
                            null
                        }
                    }
                }

                val responses = deferredResponses.awaitAll().filterNotNull()

                if (responses.isNotEmpty()) {
                    val evaluation = pythonBridge.evaluate(prompt, Gson().toJson(responses))
                    _evaluationResults.value = evaluation
                    
                    // Save to history
                    evaluation.forEach { res ->
                        repository.insertHistory(
                            HistoryEntity(
                                prompt = prompt,
                                modelName = res.model,
                                response = responses.find { it["model"] == res.model }?.get("text") ?: "",
                                timestamp = System.currentTimeMillis(),
                                accuracy = res.accuracy,
                                coherence = res.coherence,
                                perplexity = res.perplexity,
                                bleu = res.bleu,
                                rouge = res.rouge,
                                semanticSimilarity = res.semanticSimilarity,
                                fluency = res.fluency,
                                overallScore = res.overallScore
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getAllHistory(): Flow<List<HistoryEntity>> = repository.getAllHistory()

    fun searchHistory(query: String): Flow<List<HistoryEntity>> = repository.searchHistory(query)

    fun deleteHistory(id: Long) {
        viewModelScope.launch {
            repository.deleteHistory(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.deleteAllHistory()
        }
    }
}
