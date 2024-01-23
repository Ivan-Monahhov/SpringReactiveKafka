package com.example.springreactivekafka.example2

import java.time.LocalDate
import java.util.*

@Suppress("MagicNumber")
data class RanKpiKafka(
    val id: String = UUID.randomUUID().toString(),
    // cellname, kpi, start_date, end_date, numofdays
    val cellname: String = "",
    val kpi: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val numofdays: Int = -1,
)
