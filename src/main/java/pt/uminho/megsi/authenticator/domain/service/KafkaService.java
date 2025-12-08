package pt.uminho.megsi.authenticator.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import pt.uminho.megsi.authenticator.application.dto.EmailDto;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, EmailDto> kafkaTemplate;

    public void send(String topic, EmailDto message) {
        CompletableFuture<SendResult<String, EmailDto>> future = kafkaTemplate.send(topic, message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sent message: [{}}] with offset=[{}}]", message, result.getRecordMetadata().offset());
            } else {
                log.error("Unable to send message: [{}}] due to : ", message, ex);
            }
        });
    }
}
