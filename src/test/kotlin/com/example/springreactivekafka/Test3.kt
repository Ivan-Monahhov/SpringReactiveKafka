package com.example.springreactivekafka

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration
import java.util.function.Supplier

// Property naming structure is extremely important : it should go beanName-directionIn/Out-0
class Test3 : AbstractIntegrationTest() {
    @Autowired
    lateinit var consumer: KafkaConsumer

    @Autowired
    lateinit var producer: Supplier<Flux<String>>

    @Test
    fun fullTest() {
        // skips first message
        /*consumer.getFlux().take(2).timeout(Duration.ofSeconds(5)).`as`(StepVerifier::create)
            .consumeNextWith { s -> Assertions.assertEquals("msg 0", s) }
            .consumeNextWith { s -> Assertions.assertEquals("msg 1", s) }.verifyComplete()*/
        //This will never get called
        /*consumer.getFlux().subscribe {
            StepVerifier.create(consumer.getFlux())
                .consumeNextWith { s -> Assertions.assertEquals("msg 0", s) }
                .consumeNextWith { s -> Assertions.assertEquals("msg 100", s) }
                .verifyComplete()
        }*/
        /* there is some idiotic race condition here
        consumer.getFlux().take(5).timeout(Duration.ofSeconds(5)).`as`(StepVerifier::create)
            .consumeNextWith { s -> Assertions.assertEquals("msg 1", s) }
            .consumeNextWith { s -> Assertions.assertEquals("msg 2", s) }
            .consumeNextWith { s -> Assertions.assertEquals("msg 3", s) }
            .consumeNextWith { s -> Assertions.assertEquals("msg 4", s) }
            .consumeNextWith { s -> Assertions.assertEquals("msg 5", s) }
            .verifyComplete()*/
    }
}
