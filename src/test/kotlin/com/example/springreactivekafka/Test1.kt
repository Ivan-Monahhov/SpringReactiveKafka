package com.example.springreactivekafka

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.TestPropertySource
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.test.StepVerifier
import java.time.Duration
import java.util.function.Consumer

@TestPropertySource(properties = ["spring.cloud.stream.bindings.testConsumer-in-0.destination=input-topic", "spring.cloud.function.definition=producer;testConsumer"])
class KafkaProducerTest : AbstractIntegrationTest() {
    // https://github.com/vinsguru/reactive-event-driven-microservices/tree/master
    @Autowired
    lateinit var consumer: Consumer<Flux<String?>>

    @Test
    fun producerTest() {
        sink.asFlux()
            .take(2)
            .timeout(Duration.ofSeconds(5))
            .`as`(StepVerifier::create)
            .consumeNextWith { s -> Assertions.assertEquals("msg 0", s) }
            .consumeNextWith { s -> Assertions.assertEquals("msg 1", s) }
            .verifyComplete()
        /*consumer.andThen { f ->
            f.take(2).timeout(Duration.ofSeconds(5)).`as`(StepVerifier::create)
                .consumeNextWith { s -> Assertions.assertEquals("msg 0", s) }
                .consumeNextWith { s -> Assertions.assertEquals("msg 1", s) }
                .verifyComplete()
        }*/
    }

    @TestConfiguration
    internal class TestConfig {
        @Bean
        fun testConsumer(): Consumer<Flux<String>> {
            return Consumer { f: Flux<String> ->
                f.doOnNext { t: String ->
                    sink.tryEmitNext(
                        t,
                    )
                }.subscribe()
            }
        }
    }

    companion object {
        private val sink = Sinks.many().unicast().onBackpressureBuffer<String>()
    }
}
