package pl.blaszak.ai.rag

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.blaszak.ai.rag.model.SearchResult

class MergeCloserChunksTest {

    @Test
    fun shouldMergeSimilarDocuments() {
        // given
        val input = listOf(
            SearchResult("1_id", "first text. ", "second.md", 0.8),
            SearchResult("2_id", "Second text. ", "first.md", 0.6),
            SearchResult("3_id", "Third text. ", "first.md", 0.83),
            SearchResult("4_id", "Fourth text. ", "second.md", 0.81),
            SearchResult("5_id", "Fifth text. ", "first.md", 0.84),
            SearchResult("6_id", "Sixth text. ", "first.md", 0.61),
        )
        // when
        val merged = input.mergeCloserChunks()

        //than
        assertThat(merged).isNotNull()
        assertThat(merged).hasSize(3)
    }
}