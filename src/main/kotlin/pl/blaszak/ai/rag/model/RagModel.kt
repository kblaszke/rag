package pl.blaszak.ai.rag.model

data class SearchResult(
    val id: String,
    var text: String,
    val fileName: String,
    val score: Double
)

data class RagResponse (
    val conversationId: String,
    val answer: String,
)