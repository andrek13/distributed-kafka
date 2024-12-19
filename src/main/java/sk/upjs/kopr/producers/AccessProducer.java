package sk.upjs.kopr.producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sk.upjs.kopr.entities.Access;


@Component
public class AccessProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    private String topic = "access";

    private ObjectMapper objectMapper = new ObjectMapper();

    private AccessProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 2000)
    public void sendAccess() {
        Access access = Access.randomAccess();
      //  String routingKey = "access." + access.getLocation();
        try {
            String accessJson = objectMapper.writeValueAsString(access);
            System.out.println("Sending access: " + accessJson);
            kafkaTemplate.send(topic, access.getCard() + "", accessJson);
        } catch (Exception e) {
            System.err.println("Failed to serialize access: " + e.getMessage());
        }
    }

}
