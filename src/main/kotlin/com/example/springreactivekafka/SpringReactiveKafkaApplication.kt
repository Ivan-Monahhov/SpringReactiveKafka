package com.example.springreactivekafka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringReactiveKafkaApplication

fun main(args: Array<String>) {
    runApplication<SpringReactiveKafkaApplication>(*args)
}
