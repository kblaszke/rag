package pl.blaszak.ai.rag.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "statistic.cleanup")
class StatisticCleanUpProperty {
    lateinit var cron: String
    var retentionDays: Long = 14
}