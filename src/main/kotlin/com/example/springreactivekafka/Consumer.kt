package com.example.springreactivekafka
/*
    goal: to demo a simple kafka consumer using java functional interfaces
 */
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import java.util.function.Consumer

@Configuration
class KafkaConsumer {
    @Bean
    fun consumer(): Consumer<Flux<String?>> {
        return Consumer<Flux<String?>> { flux: Flux<String?> ->
            flux
                .doOnNext { s ->
                    log.info(
                        "consumer received {}",
                        s
                    )
                }
                .subscribe()
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(KafkaConsumer::class.java)
    }
}
