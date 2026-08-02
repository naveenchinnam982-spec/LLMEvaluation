package project.llmevaluation.repository

import project.llmevaluation.api.ApiService
import project.llmevaluation.data.HistoryDao
import project.llmevaluation.data.HistoryEntity
import project.llmevaluation.models.LLMRequest
import project.llmevaluation.models.LLMResponse
import retrofit2.Response
import kotlinx.coroutines.flow.Flow

class LLMRepository(
    private val apiService: ApiService,
    private val historyDao: HistoryDao
) {
    suspend fun getCompletion(apiKey: String, request: LLMRequest): Response<LLMResponse> {
        return apiService.getCompletion(apiKey = "Bearer $apiKey", request = request)
    }

    suspend fun insertHistory(history: HistoryEntity) {
        historyDao.insertHistory(history)
    }

    fun getAllHistory(): Flow<List<HistoryEntity>> {
        return historyDao.getAllHistory()
    }

    fun searchHistory(query: String): Flow<List<HistoryEntity>> {
        return historyDao.searchHistory(query)
    }

    suspend fun deleteHistory(id: Long) {
        historyDao.deleteHistory(id)
    }

    suspend fun deleteAllHistory() {
        historyDao.deleteAllHistory()
    }
}
