package com.example.springreactivekafka

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.function.Supplier

/*
   goal: to demo a simple kafka producer using java functional interfaces
*/
@Configuration
class KafkaProducer {
    //    @Bean
    //    public SenderOptionsCustomizer customizer(){
    //        return (s, so) -> so.producerProperty(ProducerConfig.ACKS_CONFIG, "all")
    //                .producerProperty(ProducerConfig.BATCH_SIZE_CONFIG, "20001");
    //    }
    @Bean
    fun producer(): Supplier<Flux<String>> {
        return Supplier<Flux<String>> {
            Flux.interval(Duration.ofSeconds(1))
                .take(10)
                .map { i -> "msg $i" }
                .doOnNext { m -> log.info("produced {}", m) }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(KafkaProducer::class.java)
    }
}
