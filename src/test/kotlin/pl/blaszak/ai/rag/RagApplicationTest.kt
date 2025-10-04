package pl.blaszak.ai.rag

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.blaszak.ai.rag.model.mergeCloserChunks
import pl.blaszak.ai.rag.model.toSearchResults

@SpringBootTest
class RagApplicationTest() {

    @Autowired
    var vectorStore: VectorStore? = null

    @Autowired
    val chatModel: OpenAiChatModel? = null


    @Test
    fun `should include sources in response`() {
        // given
        val question = "Jak być radufny?"
        val similarDocuments = vectorStore?.similaritySearch(question)?.map { it.toSearchResults() }
        val fragments = similarDocuments?.mergeCloserChunks()
            ?.joinToString(separator = "\n") { it.text }?.take(20000).toString()
        val prompt = createPrompt(question, fragments)
        val message = UserMessage.builder().text(prompt).build()
        // when
        val response = chatModel?.call(Prompt(listOf(message)))
        // then
        val stringResponse = response?.result?.output?.text.toString()
        assertThat(usage("radufny", stringResponse)).isGreaterThanOrEqualTo(usage("radufny", fragments))
    }

    fun usage(word: String, text: String) = Regex("\\b$word\\b", RegexOption.IGNORE_CASE).findAll(text).count()

    private fun createPrompt(question: String, fragments: String) =
        """Odpowiedz na pytanie na podstawie poniższych fragmentów

Fragmenty:
$fragments
      
Pytanie:
$question
        
""".trimIndent()
}

