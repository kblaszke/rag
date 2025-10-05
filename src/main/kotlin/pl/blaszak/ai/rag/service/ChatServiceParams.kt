package pl.blaszak.ai.rag.service

import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.vectorstore.VectorStore
import pl.blaszak.ai.rag.repository.LocalDbMessageRepository
import pl.blaszak.ai.rag.repository.LocalDbStatisticRepository

data class ChatServiceParams (
    var vectorStore: VectorStore? = null,
    var localDbMessageRepository: LocalDbMessageRepository? = null,
    var localDbStatisticRepository: LocalDbStatisticRepository? = null,
    var chatModel: OpenAiChatModel? = null,
    var maxTokens: Int = 2000
)

fun fromParams(params: ChatServiceParams): ChatService = ChatService(
    requireNotNull(params.vectorStore) { "vectorStore is required" },
    requireNotNull(params.localDbMessageRepository) { "localDbMessageRepository is required" },
    requireNotNull(params.localDbStatisticRepository) { "localDbStatisticRepository is required" },
    requireNotNull(params.chatModel) { "chatModel is required" },
    params.maxTokens
)

fun chatService(init: ChatServiceParams.() -> Unit) =  fromParams(ChatServiceParams().apply(init))