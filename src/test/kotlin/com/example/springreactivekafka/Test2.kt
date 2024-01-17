package com.example.springreactivekafka

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

// Property naming structure is extremely important : it should go beanName-directionIn/Out-0
@TestPropertySource(properties = ["spring.cloud.stream.bindings.testProducer-out-0.destination=input-topic", "spring.cloud.function.definition=testProducer;consumer"])
@ExtendWith(OutputCaptureExtension::class)
class Test2 : AbstractIntegrationTest() {
    @Autowired
    lateinit var consumer: Consumer<Flux<String?>>

    @Test
    fun consumerTest(output: CapturedOutput) {
        Mono.delay(Duration.ofMillis(2000))
            .then(Mono.fromSupplier { output.out })
            .`as`(StepVerifier::create)
            .consumeNextWith { s -> Assertions.assertTrue(s.contains("consumer received msg hello world")) }
            .verifyComplete()
    }

    /*
            return Supplier<Flux<String>> {
            Flux.interval(Duration.ofSeconds(1))
                .take(10)
                .map { i -> "msg $i" }
                .doOnNext { m -> log.info("produced {}", m) }
        }
     */
    companion object {
        private val log = LoggerFactory.getLogger(Test2::class.java)
    }

    @TestConfiguration
    class TestConfig {
        @Bean
        fun testProducer(): Supplier<Flux<String>> {
            return Supplier<Flux<String>> {
                Flux.just<String>("hello world").take(1).map { i -> "msg $i" }
                    .doOnNext { m -> log.info("produced {}", m) }
            }
        }

    }
}
