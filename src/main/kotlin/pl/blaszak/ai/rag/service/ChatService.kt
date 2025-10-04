package pl.blaszak.ai.rag.service

import kotlinx.coroutines.flow.flowOf
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.vectorstore.VectorStore
import pl.blaszak.ai.rag.model.LocalDbRole
import pl.blaszak.ai.rag.model.RagResponse
import pl.blaszak.ai.rag.model.createFragments
import pl.blaszak.ai.rag.model.toUserMessageList
import pl.blaszak.ai.rag.repository.LocalDbMessageRepository
import pl.blaszak.ai.rag.repository.LocalDbStatisticRepository
import pl.blaszak.ai.rag.repository.save
import pl.blaszak.ai.rag.textResponse
import java.util.UUID

class ChatService(
    val vectorStore: VectorStore,
    val localDbMessageRepository: LocalDbMessageRepository,
    val localDbStatisticRepository: LocalDbStatisticRepository,
    val chatModel: OpenAiChatModel,
    val maxTokens: Int
) {
    companion object {
        const val INIT_ASSISTANT_MESSAGE =
            "Wciel się w rolę oświeconego, bardzo inteligentnego i dowcipnego nauczyciela Dhammy. Opowiaaj w pierwszej osobie jak byś był samym Ajahnem Brahmem"
    }

    fun initConversation() = UUID.randomUUID().toString()
        .also { localDbMessageRepository.save(it, LocalDbRole.ASSISTANT, INIT_ASSISTANT_MESSAGE) }


    fun handleStream(
        conversationId: String?,
        question: String
    ) = flowOf(handle(conversationId, question))

    fun handle(
        conversationId: String?,
        question: String,
    ): RagResponse {
        val conversationId = if (conversationId.isNullOrEmpty()) initConversation() else conversationId
        val fragments = vectorStore.similaritySearch(question).createFragments()
        val prompt = createPrompt(question, fragments.joinToString("\n").take(maxTokens))
        localDbMessageRepository.save(conversationId, LocalDbRole.USER, prompt)
        val textResponse = localDbMessageRepository.findByConversationId(conversationId)
            .toUserMessageList()
            .let { chatModel.call(Prompt(it)).textResponse() }
        localDbMessageRepository.save(conversationId, LocalDbRole.SYSTEM, textResponse)
        localDbStatisticRepository.save(question, fragments, textResponse)
        return RagResponse(conversationId, textResponse)
    }

    private fun createPrompt(question: String, fragments: String) =
        """Odpowiedz na pytanie na podstawie poniższych fragmentów

Fragmenty:
$fragments
      
Pytanie:
$question
        
""".trimIndent()
}
