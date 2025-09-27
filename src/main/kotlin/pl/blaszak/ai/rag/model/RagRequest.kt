package pl.blaszak.ai.rag.model

data class RagRequest(
    val conversationId: String?,
    val message: String
)