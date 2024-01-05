package ru.isimplelab.absws.wallet.config;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class WalletListener {

    private final ReactiveKafkaConsumerTemplate<String, String> consumerTemplate;
    private final ReactiveKafkaProducerTemplate<String, String> producerTemplate;

    public WalletListener(ReactiveKafkaConsumerTemplate<String, String> consumerTemplate, ReactiveKafkaProducerTemplate<String, String> producerTemplate) {
        this.consumerTemplate = consumerTemplate;
        this.producerTemplate = producerTemplate;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        consumerTemplate
                .receiveAutoAck()
                .doOnNext(System.out::println)
                .flatMap(v -> producerTemplate.send("kafka_topic_answer", "LALALA" + new Random().nextInt()))
                .subscribe();
    }
}
