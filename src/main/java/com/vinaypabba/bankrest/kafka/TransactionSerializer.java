package com.vinaypabba.bankrest.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinaypabba.bankrest.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class TransactionSerializer implements Serializer<Transaction> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {
        // No configuration currently
    }

    @Override
    public byte[] serialize(String s, Transaction transaction) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsBytes(transaction);
        } catch (JsonProcessingException e) {
            log.error("Couldn't parse transaction to JSON object, e");
        }
        return retVal;
    }

    @Override
    public void close() {
        // No configuration currently
    }
}
