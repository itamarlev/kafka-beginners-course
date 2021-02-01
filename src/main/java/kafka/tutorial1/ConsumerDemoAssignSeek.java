package kafka.tutorial1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoAssignSeek {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ConsumerDemoAssignSeek.class.getName());
        Properties properties = new Properties();
        String bootstrapServers = "localhost:9092";
        String topic = "first_topic";

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // create a consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        int partitionToReadFrom = 0;
        long offsetToReadFrom = 16L;

        // assign
        TopicPartition topicAndPartitionToReadFrom = new TopicPartition(topic, partitionToReadFrom);
        consumer.assign(Arrays.asList(topicAndPartitionToReadFrom));

        // seek
        consumer.seek(topicAndPartitionToReadFrom, offsetToReadFrom);

        int numberOfmessagesToRead = 5;
        int numberOfMessagesReadSoFar = 0;

        // poll for new data
        while (numberOfMessagesReadSoFar < numberOfmessagesToRead) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord record : records) {
                logger.info("Key: " + record.key() + " ,Value: " + record.value());
                logger.info("Partition: " + record.partition() + ", Offset: " + record.offset());
                numberOfMessagesReadSoFar++;
                if(numberOfMessagesReadSoFar >= numberOfmessagesToRead){
                    break;
                }
            }
        }
    }
}
