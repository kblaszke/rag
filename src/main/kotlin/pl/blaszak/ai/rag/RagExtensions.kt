package pl.blaszak.ai.rag

import org.springframework.ai.document.Document
import pl.blaszak.ai.rag.model.SearchResult

fun Document.toSearchResults(): SearchResult =
    SearchResult(
        id = id,
        text = text ?: "",
        fileName = (metadata["fileName"] ?: "unknown") as String,
        score = score ?: 0.toDouble()
    )

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
