package project.llmevaluation.models

import com.google.gson.annotations.SerializedName

data class LLMResponse(
    @SerializedName("id") val id: String,
    @SerializedName("choices") val choices: List<Choice>,
    @SerializedName("model") val model: String,
    @SerializedName("usage") val usage: Usage? = null
)

data class Choice(
    @SerializedName("message") val message: Message,
    @SerializedName("finish_reason") val finishReason: String? = null
)

data class Usage(
    @SerializedName("prompt_tokens") val promptTokens: Int,
    @SerializedName("completion_tokens") val completionTokens: Int,
    @SerializedName("total_tokens") val totalTokens: Int
)
