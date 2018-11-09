package com.vinaypabba.bankrest.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class TransactionPartitioner implements Partitioner {

    private static final int PARTITION_COUNT=50;

    @Override
    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        Integer keyInt=Integer.parseInt(o.toString());
        return keyInt % PARTITION_COUNT;
    }

    @Override
    public void close() {
        // No configuration currently
    }

    @Override
    public void configure(Map<String, ?> map) {
        // No configuration currently
    }

}
