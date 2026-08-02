package project.llmevaluation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import project.llmevaluation.python.PythonBridge
import project.llmevaluation.repository.LLMRepository

class LLMViewModelFactory(
    private val repository: LLMRepository,
    private val pythonBridge: PythonBridge
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LLMViewModel::class.java)) {
            return LLMViewModel(repository, pythonBridge) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
