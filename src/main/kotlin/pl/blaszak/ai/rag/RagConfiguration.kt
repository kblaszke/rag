package pl.blaszak.ai.rag

import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.blaszak.ai.rag.service.ChatService

@Configuration
class RagConfiguration {

    @Bean
    fun chatService(
        vectorStore: VectorStore,
        localDbMessageRepository: LocalDbMessageRepository,
        chatModel : OpenAiChatModel
    ) = ChatService(
        vectorStore,
        localDbMessageRepository,
        chatModel,
        2000
    )
}