package pl.blaszak.ai.rag.configuration

import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pl.blaszak.ai.rag.repository.LocalDbMessageRepository
import pl.blaszak.ai.rag.repository.LocalDbStatisticRepository
import pl.blaszak.ai.rag.service.ChatService
import pl.blaszak.ai.rag.service.StatisticService
import pl.blaszak.ai.rag.service.chatService
import pl.blaszak.ai.rag.service.statisticService

@Configuration
class RagConfiguration {

    @Bean
    fun chatServiceBean(
        vectorStore: VectorStore,
        localDbMessageRepository: LocalDbMessageRepository,
        localDbStatisticRepository: LocalDbStatisticRepository,
        chatModel : OpenAiChatModel
    ): ChatService = chatService {
        this.vectorStore = vectorStore
        this.localDbMessageRepository = localDbMessageRepository
        this.localDbStatisticRepository = localDbStatisticRepository
        this.chatModel = chatModel
    }

    @Bean
    fun statisticServiceBean(
        localDbStatisticRepository: LocalDbStatisticRepository,
        cleanUpProperty: StatisticCleanUpProperty
    ): StatisticService = statisticService {
        this.localDbStatisticRepository = localDbStatisticRepository
        this.statisticCleanUpProperty = cleanUpProperty
    }

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