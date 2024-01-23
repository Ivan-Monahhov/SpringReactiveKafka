package com.example.springreactivekafka

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.annotation.DirtiesContext
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions
import reactor.kafka.sender.SenderRecord
import java.util.function.UnaryOperator

@DirtiesContext
@SpringBootTest(properties = ["logging.level.root=INFO", "spring.cloud.stream.kafka.binder.configuration.auto.offset.reset=earliest"])
@EmbeddedKafka(
    partitions = 1,
    bootstrapServersProperty = "spring.kafka.bootstrap-servers",
    brokerProperties = ["log4j.rootLogger=Trace, kafka", "log4j.appender.kafka=com.cloudera.kafka.log4jappender.KafkaLog4jAppender", "log4j.appender.stdout=org.apache.log4j.ConsoleAppender", "log4j.appender.stdout.Target=System.out"],
)
public abstract class AbstractIntegrationTest {
    @Autowired
    lateinit var broker: EmbeddedKafkaBroker
    protected fun <K, V> createSender(builder: UnaryOperator<SenderOptions<K, V>>): KafkaSender<K, V> {
        var options = builder.apply(SenderOptions.create(KafkaTestUtils.producerProps(broker)))
        return KafkaSender.create(options)
    }

    protected fun <K, V> toSenderRecord(topic: String?, key: K, value: V): SenderRecord<K, V, K> {
        return SenderRecord.create(topic, null, null, key, value, key)
    }
}
