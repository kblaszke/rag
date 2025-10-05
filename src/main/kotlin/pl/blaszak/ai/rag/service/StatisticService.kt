package pl.blaszak.ai.rag.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.scheduling.annotation.Scheduled
import pl.blaszak.ai.rag.configuration.StatisticCleanUpProperty
import pl.blaszak.ai.rag.repository.LocalDbStatisticRepository
import java.time.LocalDateTime

class StatisticService(
    val localDbStatisticRepository: LocalDbStatisticRepository,
    val statisticCleanUpProperty: StatisticCleanUpProperty
): ApplicationListener<ApplicationReadyEvent> {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(cron = "\${statistic.cleanup.cron}")
    fun cleanupOldStatistics() {
        val cutoff = LocalDateTime.now().minusDays(statisticCleanUpProperty.retentionDays)
        val deletedCount = localDbStatisticRepository.deleteByCreatedAtBefore(cutoff)
        logger.info("Clean up: $deletedCount records from LocalDbStatisticRepository has been deleted")
    }

    override fun onApplicationEvent(event: ApplicationReadyEvent) = cleanupOldStatistics()

}