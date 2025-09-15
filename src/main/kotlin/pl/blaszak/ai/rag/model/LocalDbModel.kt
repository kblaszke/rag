package pl.blaszak.ai.rag.model

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.NamedQueries
import org.hibernate.annotations.NamedQuery
import java.time.LocalDateTime

enum class LocalDbRole() {
    USER,
    ASSISTANT,
    SYSTEM
}

@Entity
@NamedQueries(
    NamedQuery(name = "LocalDbMessage.findByConversationId", query = "select m from LocalDbMessage m where m.conversationId = ?1")
)
class LocalDbMessage(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var conversationId: String,
    @Enumerated(EnumType.STRING)
    var localDbRole: LocalDbRole,   // "user", "assistant", "system"
    var text: String,
    var creationDateTime: LocalDateTime? = null
) {
    constructor() : this(null, "", LocalDbRole.USER, "", null)
    constructor(conversationId: String, localDbRole: LocalDbRole, text: String) :
            this(null, conversationId, localDbRole, text, LocalDateTime.now())
}