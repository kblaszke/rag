package pl.blaszak.ai.rag.service

import pl.blaszak.ai.rag.configuration.StatisticCleanUpProperty
import pl.blaszak.ai.rag.repository.LocalDbStatisticRepository

data class StatisticServiceParams (
    var localDbStatisticRepository: LocalDbStatisticRepository? = null,
    var statisticCleanUpProperty: StatisticCleanUpProperty? = null
)

fun fromParams(params: StatisticServiceParams) = StatisticService(
    localDbStatisticRepository = requireNotNull(params.localDbStatisticRepository) { "localDbStatisticRepository is required" },
    statisticCleanUpProperty = requireNotNull(params.statisticCleanUpProperty) { "statisticCleanUpProperty is required"}
)

fun statisticService(init: StatisticServiceParams.() -> Unit) = fromParams(StatisticServiceParams().apply (init))