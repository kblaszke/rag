package pl.blaszak.ai.rag.model

import org.springframework.ai.document.Document

fun Document.toSearchResults(): SearchResult =
    SearchResult(
        id = id,
        text = text ?: "",
        fileName = (metadata["fileName"] ?: "unknown") as String,
        score = score ?: 0.toDouble()
    )

fun List<Document>.createFragments() = this
    .map { it.toSearchResults() }
    .mergeCloserChunks()
    .map { it.text }

fun List<SearchResult>.mergeCloserChunks(): List<SearchResult> =
    sortedByDescending { it.score }.fold(mutableListOf()) { merged, element ->
        if (merged.isEmpty() || merged.last().fileName != element.fileName) {
            merged.add(element)
        } else {
            val last = merged.removeAt(merged.lastIndex)
            merged.add(
                SearchResult(
                    last.id,
                    last.text + element.text,
                    last.fileName,
                    element.score
                )
            )
        }
        merged
    }
