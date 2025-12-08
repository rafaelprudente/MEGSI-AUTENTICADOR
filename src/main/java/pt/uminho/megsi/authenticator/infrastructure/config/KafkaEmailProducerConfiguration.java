package pt.uminho.megsi.authenticator.infrastructure.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import pt.uminho.megsi.authenticator.application.dto.EmailDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaEmailProducerConfiguration {
    @Value("${spring.kafka.bootstrap-servers}")
    String bootstrapAddress;

    @Bean
    public ProducerFactory<String, EmailDto> emailProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, EmailDto> emailKafkaTemplate() {
        return new KafkaTemplate<>(emailProducerFactory());
    }
}
