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

fun fromConfig(cfg: ChatServiceParams): ChatService = ChatService(
    requireNotNull(cfg.vectorStore) { "vectorStore is required" },
    requireNotNull(cfg.localDbMessageRepository) { "localDbMessageRepository is required" },
    requireNotNull(cfg.localDbStatisticRepository) { "localDbStatisticRepository is required" },
    requireNotNull(cfg.chatModel) { "chatModel is required" },
    cfg.maxTokens
)

fun chatService(init: ChatServiceParams.() -> Unit) =  fromConfig(ChatServiceParams().apply(init))