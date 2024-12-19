package sk.upjs.kopr.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import sk.upjs.kopr.entities.Access;
import sk.upjs.kopr.entities.Card;
import sk.upjs.kopr.repositories.AccessRepository;
import sk.upjs.kopr.repositories.CardRepository;

@Component
public class AccessConsumer {


    private final AccessRepository accessRepository;

    private final CardRepository cardRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public AccessConsumer(AccessRepository accessRepository, CardRepository cardRepository) {
        this.accessRepository = accessRepository;
        this.cardRepository = cardRepository;
    }

    @KafkaListener(topics = "access")
    public void receiveAccess(String accessJson) {
        System.out.println("Received access: " + accessJson);

        try {
            Access access = objectMapper.readValue(accessJson, Access.class);
            System.out.println("Received access: " + access);

            // Validate card existence
            Card card = cardRepository.findById(access.getCard()).orElseThrow(() -> new Exception("Unknown card"));

            System.err.println("Card: " + card.toString());
            // Check the latest access direction for the card
            Access latestAccess = accessRepository.findTopByCardIdOrderByTimestampDesc(card.getCardId());
            System.out.println("Latest access: " + latestAccess);
            String latestDirection = (latestAccess != null) ? latestAccess.getDirection() : "OUT";

            if ("IN".equals(latestDirection) && "IN".equals(access.getDirection())) {
                System.err.println("Person is already inside and cannot enter again");
                throw new Exception("Invalid direction");
            } else if ("OUT".equals(latestDirection) && "OUT".equals(access.getDirection())) {
                System.err.println("Person is already outside and cannot exit again");
                throw new Exception("Invalid direction");
            }

            // Save the new access record
            access.setCard(card.getCardId());
            accessRepository.save(access);

        }  catch (Exception e) {
            System.err.println("Failed to process message: " + e.getMessage());
        }
    }
}
