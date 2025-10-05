package pl.blaszak.ai.rag.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional
import pl.blaszak.ai.rag.model.LocalDbStatistic
import java.time.LocalDateTime

interface LocalDbStatisticRepository: CrudRepository<LocalDbStatistic, Long> {
    @Transactional
    fun deleteByCreatedAtBefore(cutoffDate: LocalDateTime): Long
}

fun LocalDbStatisticRepository.save(question: String, fragments: List<String>, answer: String ) = LocalDbStatistic(question, fragments, answer).apply { save(this) }

