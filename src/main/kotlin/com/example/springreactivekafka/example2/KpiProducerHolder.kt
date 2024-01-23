package com.example.springreactivekafka.example2

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.util.function.Supplier

@Configuration
class KpiProducerHolder {
    // if we make it unicast it will not work
    val kpiSink = Sinks.many().multicast().onBackpressureBuffer<RanKpiKafka>()

    @Bean
    fun kpiProducer(): Supplier<Flux<RanKpiKafka>> {
        kpiSink.asFlux().subscribe { kpi -> log.info("Produced $kpi") }
        return Supplier<Flux<RanKpiKafka>> {
            kpiSink.asFlux()
            //Flux.just<RanKpiKafka>(RanKpiKafka(cellname = "test", kpi = "test", numofdays = 4))
            //    .map { i -> i }
            //    .doOnNext { m -> log.info("produced {}", m) }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(KpiProducerHolder::class.java)
    }
}
