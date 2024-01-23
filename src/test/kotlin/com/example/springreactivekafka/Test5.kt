package com.example.springreactivekafka

import com.example.springreactivekafka.example2.KpiConsumerHolder
import com.example.springreactivekafka.example2.KpiProducerHolder
import com.example.springreactivekafka.example2.RanKpiKafka
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.test.context.TestPropertySource
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration
import java.util.function.Consumer
import java.util.function.Supplier

@TestPropertySource(properties = [ "spring.cloud.function.definition=kpiConsumer;kpiProducer"])
@ExtendWith(OutputCaptureExtension::class)
class Test5 : AbstractIntegrationTest() {
    @Autowired
    lateinit var kpiConsumerHolder: KpiConsumerHolder

    @Autowired
    lateinit var kpiProducerHolder: KpiProducerHolder

    @Autowired
    lateinit var kpiConsumer: Consumer<Flux<RanKpiKafka?>>

    @Autowired
    lateinit var kpiProducer: Supplier<Flux<RanKpiKafka>>

    @Test
    fun producerTest(output: CapturedOutput) {
        kpiProducerHolder.kpiSink.tryEmitNext(RanKpiKafka(cellname = "test", kpi = "test", numofdays = 4))
        Mono.delay(Duration.ofMillis(5000))
            .then(Mono.fromSupplier { output.out })
            .`as`(StepVerifier::create)
            .consumeNextWith { s -> Assertions.assertTrue(s.contains("Consumed RanKpiKafka")) }
            .verifyComplete()
    }
}