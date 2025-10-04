package pl.blaszak.ai.rag

import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pl.blaszak.ai.rag.service.ChatService
import org.springframework.web.servlet.config.annotation.CorsRegistry
import pl.blaszak.ai.rag.repository.LocalDbMessageRepository
import pl.blaszak.ai.rag.repository.LocalDbStatisticRepository


@Configuration
class RagConfiguration {

    @Bean
    fun chatService(
        vectorStore: VectorStore,
        localDbMessageRepository: LocalDbMessageRepository,
        localDbStatisticRepository: LocalDbStatisticRepository,
        chatModel : OpenAiChatModel
    ) = ChatService(
        vectorStore,
        localDbMessageRepository,
        localDbStatisticRepository,
        chatModel,
        2000
    )

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:4200")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
            }
        }
    }
}