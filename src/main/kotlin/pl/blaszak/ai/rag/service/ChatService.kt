package pl.blaszak.ai.rag.service

import kotlinx.coroutines.flow.flowOf
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.vectorstore.VectorStore
import pl.blaszak.ai.rag.LocalDbMessageRepository
import pl.blaszak.ai.rag.model.LocalDbRole
import pl.blaszak.ai.rag.mergeCloserChunks
import pl.blaszak.ai.rag.model.LocalDbMessage
import pl.blaszak.ai.rag.model.RagResponse
import pl.blaszak.ai.rag.toSearchResults
import java.util.UUID

class ChatService(
    val vectorStore: VectorStore,
    val localDbMessageRepository: LocalDbMessageRepository,
    val chatModel: OpenAiChatModel,
    val maxTokens: Int
) {
    companion object {
        const val INIT_ASSISTANT_MESSAGE =
            "Wciel się w rolę oświeconego, bardzo inteligentnego i owcipnego mauczyciela Dhammy. Opowiaaj w pierwszej osobie jak byś był samym Ajahnem Brahmem"
    }

    fun initConversation(): String {
        val conversationId = UUID.randomUUID().toString()
        val localDbMessage = LocalDbMessage(conversationId, LocalDbRole.ASSISTANT, INIT_ASSISTANT_MESSAGE)
        localDbMessageRepository.save<LocalDbMessage>(localDbMessage)
        return conversationId
    }

    fun handleStream(
        conversationId: String?,
        question: String,
        attachDocumentation: Boolean
    // ) = Flux.just<RagResponse>(handle(conversationId, question, attachDocumentation))
    ) = flowOf(RagResponse("tmpConversationId", "Blaszak is king!"))

    fun handle(
        conversationId: String?,
        question: String,
        attachDocumentation: Boolean
    ): RagResponse {
        val conversationId = if (conversationId.isNullOrEmpty()) initConversation() else conversationId
        val prompt = if (attachDocumentation) createPrompt(question) else question
        val localDbMessage = LocalDbMessage(conversationId, LocalDbRole.USER, prompt)
        localDbMessageRepository.save<LocalDbMessage>(localDbMessage)
        val messages = localDbMessageRepository.findByConversationId(conversationId)
        val userMessages = messages.map { localDbMessage -> UserMessage.builder().text(localDbMessage.text).build() }
        val response = chatModel.call(Prompt(userMessages))
        val textResponse = response.result.output.text.toString()
        val responseDbMessage = LocalDbMessage(conversationId, LocalDbRole.SYSTEM, textResponse)
        localDbMessageRepository.save<LocalDbMessage>(responseDbMessage)
        return RagResponse(conversationId, textResponse)
    }

    private fun createPrompt(question: String): String {
        val similarDocuments = vectorStore.similaritySearch(question).map { it.toSearchResults() }
        val fragments = similarDocuments.mergeCloserChunks()
            .joinToString(separator = "\n") { it.text }.take(maxTokens)
        return createPrompt(question, fragments)
    }

    private fun createPrompt(question: String, fragments: String) =
        """Odpowiedz na pytanie na podstawie poniższych fragmentów

Fragmenty:
$fragments
      
Pytanie:
$question
        
""".trimIndent()
}
