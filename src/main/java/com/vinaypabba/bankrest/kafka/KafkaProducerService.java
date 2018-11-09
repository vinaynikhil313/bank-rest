package com.vinaypabba.bankrest.kafka;

import com.vinaypabba.bankrest.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final Producer<Long, Transaction> producer;

    public boolean pushEventsToKafka(Transaction transaction) {
        ProducerRecord<Long, Transaction> record = new ProducerRecord<>(KafkaConstants.TOPIC_NAME, transaction.getId(), transaction);
        RecordMetadata metadata;
        try {
            metadata = producer.send(record).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error in sending record", e);
            return false;
        }
        return Objects.nonNull(metadata);
    }

}
