package pl.blaszak.ai.rag.model

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import org.hibernate.annotations.NamedQueries
import org.hibernate.annotations.NamedQuery
import java.time.LocalDateTime

enum class LocalDbRole(val aiName: String) {
    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system")
}

@Entity
@NamedQueries(
    NamedQuery(name = "findByConversationId", query = "select m from LocalDbMessage m where m.conversationId = ?1")
)
class LocalDbMessage(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var conversationId: String,
    @Enumerated(EnumType.STRING)
    var localDbRole: LocalDbRole,   // "user", "assistant", "system"
    var text: String,
) {
    constructor(): this(null, "", LocalDbRole.USER, "")
}