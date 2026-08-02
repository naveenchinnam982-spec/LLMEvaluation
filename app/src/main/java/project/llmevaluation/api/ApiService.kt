package project.llmevaluation.api

import project.llmevaluation.models.LLMRequest
import project.llmevaluation.models.LLMResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("chat/completions")
    suspend fun getCompletion(
        @Header("Authorization") apiKey: String,
        @Header("HTTP-Referer") siteUrl: String = "https://github.com/project-llm-evaluation",
        @Header("X-Title") siteName: String = "LLM Evaluation Study",
        @Body request: LLMRequest
    ): Response<LLMResponse>
}
