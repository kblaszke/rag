package pl.blaszak.ai.rag

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.blaszak.ai.rag.model.RagResponse
import pl.blaszak.ai.rag.service.ChatService

@RestController
class RagRestController(val chatService: ChatService) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/api/call", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    @CrossOrigin(origins = ["http://localhost:4200"])
    fun rag(
        @RequestParam conversationId: String?,
        @RequestParam message: String
    ): Flow<ServerSentEvent<RagResponse>> {
        logger.info("ragRequest: ${conversationId}: ${message}")
        return chatService.handleStream(conversationId, message)
            .map { chunk -> ServerSentEvent.builder(chunk).build() }
            .onCompletion {  }
    }
}