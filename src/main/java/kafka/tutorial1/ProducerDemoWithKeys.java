package kafka.tutorial1;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithKeys {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ProducerDemoWithKeys.class.getName());

        String bootstrapServers = "localhost:9092";

        // create producer properties
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for (int i = 0; i <10; i++){

            // create a producer record
            String first_topic = "first_topic";
            String value = "hello world" + i;
            String key = "Key_" + i;
            ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(first_topic, key, value);

            // send data - asynchronous
            producer.send(producerRecord, (recordMetadata, e) -> {
                //executes every time a record is successfully sent or an exception is thrown
                if (e == null) {
                    logger.info("Recieved new metadata: \n" +
                            "Topic: " + recordMetadata.topic() + "\n" +
                            "Partition: " + recordMetadata.partition() + "\n" +
                            "Offset: " + recordMetadata.offset() + "\n" +
                            "Timestamp: " + recordMetadata.timestamp() + "\n");

                    System.out.println("Recieved new metadata: \n" +
                            "Topic: " + recordMetadata.topic() + "\n" +
                            "Partition: " + recordMetadata.partition() + "\n" +
                            "Offset: " + recordMetadata.offset() + "\n" +
                            "Timestamp: " + recordMetadata.timestamp() + "\n");

                } else {
                    logger.error("error while producing", e);
                }
            });

            // flush
            producer.flush();
        }

        //flush and close
        producer.close();
    }
}
