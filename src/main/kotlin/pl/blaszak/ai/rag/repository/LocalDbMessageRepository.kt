package pl.blaszak.ai.rag.repository

import org.springframework.data.repository.CrudRepository
import pl.blaszak.ai.rag.model.LocalDbMessage
import pl.blaszak.ai.rag.model.LocalDbRole

interface LocalDbMessageRepository: CrudRepository<LocalDbMessage, Long> {

    fun findByConversationId(conversationId: String): List<LocalDbMessage>
}

fun LocalDbMessageRepository.save(
    conversationId: String,
    role: LocalDbRole,
    text: String
) = LocalDbMessage(conversationId, role, text).also { save(it) }