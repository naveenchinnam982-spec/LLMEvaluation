package project.llmevaluation.models

import com.google.gson.annotations.SerializedName

data class LLMRequest(
    @SerializedName("model") val model: String,
    @SerializedName("messages") val messages: List<Message>,
    @SerializedName("temperature") val temperature: Double = 0.7,
    @SerializedName("max_tokens") val maxTokens: Int = 1000
)

data class Message(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)
