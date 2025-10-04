package pl.blaszak.ai.rag.repository

import org.springframework.data.repository.CrudRepository
import pl.blaszak.ai.rag.model.LocalDbStatistic

interface LocalDbStatisticRepository: CrudRepository<LocalDbStatistic, Long> {}

fun LocalDbStatisticRepository.save(question: String, fragments: List<String>, answer: String ) = LocalDbStatistic(question, fragments, answer).apply { save(this) }

