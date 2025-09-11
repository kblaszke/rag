package pl.blaszak.ai.rag

import org.springframework.data.repository.CrudRepository
import pl.blaszak.ai.rag.model.LocalDbMessage

interface LocalDbMessageRepository: CrudRepository<LocalDbMessage, Long> {

    fun findByConversationId(conversationId: String): List<LocalDbMessage>
}