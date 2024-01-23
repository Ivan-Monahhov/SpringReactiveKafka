package com.example.springreactivekafka

import com.example.springreactivekafka.example2.KpiConsumerHolder
import com.example.springreactivekafka.example2.KpiProducerHolder
import com.example.springreactivekafka.example2.RanKpiKafka
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.context.annotation.Bean
import org.springframework.test.context.TestPropertySource
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration
import java.util.function.Consumer
import java.util.function.Supplier

@TestPropertySource(properties = ["spring.cloud.stream.bindings.testProducer-out-0.destination=object-topic", "spring.cloud.function.definition=kpiConsumer;testProducer"])
@ExtendWith(OutputCaptureExtension::class)
class Test4 : AbstractIntegrationTest() {
    @Autowired
    lateinit var kpiConsumerHolder: KpiConsumerHolder

    @Autowired
    lateinit var kpiProducerHolder: KpiProducerHolder

    @Autowired
    lateinit var kpiConsumer: Consumer<Flux<RanKpiKafka?>>

    // @Autowired
    // lateinit var producer: Supplier<Flux<String>>

    @Test
    fun producerTest() {
        kpiProducerHolder.kpiSink.tryEmitNext(RanKpiKafka(cellname = "test", kpi = "test", numofdays = 4))
        // kpiConsumer.kpiFlux!!.take(1).timeout(Duration.ofSeconds(5)).`as`(StepVerifier::create)
        //    .consumeNextWith { s ->
        //        Assertions.assertEquals(
        //            RanKpiKafka(
        //                cellname = "test",
        //                kpi = "test",
        //                numofdays = 4,
        //            ),
        //            s,
        //        )
        //    }
        //    .verifyComplete()
        kpiProducerHolder.kpiSink.tryEmitNext(RanKpiKafka(cellname = "test", kpi = "test", numofdays = 4))
        kpiConsumerHolder.flux2!!.take(1).timeout(Duration.ofSeconds(5)).`as`(StepVerifier::create)
            .consumeNextWith { s ->
                Assertions.assertEquals(
                    RanKpiKafka(cellname = "test", kpi = "test", numofdays = 4),
                    s,
                )
            }
        kpiProducerHolder.kpiSink.tryEmitNext(RanKpiKafka(cellname = "test", kpi = "test", numofdays = 4))
    }
    // ok aso I could not get the producer working
    @Test
    fun consumerTest(output: CapturedOutput) {
        Mono.delay(Duration.ofMillis(5000))
            .then(Mono.fromSupplier { output.out })
            .`as`(StepVerifier::create)
            .consumeNextWith { s -> Assertions.assertTrue(s.contains("Consumed RanKpiKafka")) }
            .verifyComplete()
    }

    @TestConfiguration
    class TestConfig {
        @Bean
        fun testProducer(): Supplier<Flux<RanKpiKafka>> {
            return Supplier<Flux<RanKpiKafka>> {
                Flux.just<RanKpiKafka>(RanKpiKafka(cellname = "test", kpi = "test", numofdays = 4)).take(1)
                    .map { i -> i }
                    .doOnNext { m -> log.info("produced {}", m) }
            }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(Test4::class.java)
    }
}
