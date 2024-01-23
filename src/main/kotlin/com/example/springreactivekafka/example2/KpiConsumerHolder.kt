package com.example.springreactivekafka.example2

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import java.util.function.Consumer

@Configuration
class KpiConsumerHolder {
    var flux2: Flux<RanKpiKafka?>? = null

    @Bean
    fun kpiConsumer(): Consumer<Flux<RanKpiKafka?>> {
        return Consumer<Flux<RanKpiKafka?>> { flux: Flux<RanKpiKafka?> ->
            flux2 = flux
            flux.doOnNext { kpi -> println("Consumed $kpi") }.subscribe()
        }
    }
}
