package sk.upjs.kopr.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import sk.upjs.kopr.entities.Access;
import sk.upjs.kopr.kopr_rabbit.Request;
import sk.upjs.kopr.repositories.AccessRepository;
import sk.upjs.kopr.repositories.CardRepository;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class TimeSpendConsumer {

    @Autowired
   private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccessRepository accessRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    public TimeSpendConsumer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private void sendTimeSpend(String timeSpend) {
        kafkaTemplate.send("requestResponses", timeSpend);
    }

    @KafkaListener(topics = "timespend")
    public void receiveRequest(String jsonRequest) {
        try {

            Request request = requestFromJson(jsonRequest);

            System.out.println("Received request: " + request);

            int cardId = request.getCardId();
            Timestamp start = Timestamp.valueOf(request.getWhen().atStartOfDay());
            Timestamp end = Timestamp.valueOf(request.getWhen().atTime(LocalTime.MAX));

            System.out.println("Start: " + start + " End: " + end);

            System.out.println();

            Iterable<Access> accesses = accessRepository.findAccessBetween(cardId, start, end);
            System.err.println("Accesses: " + accesses.toString());
            System.out.println(LocalDate.now());
            if(accesses.equals("[]")){
                System.err.println("No accesses found for cardId " + cardId + " on " + request.getWhen());
            }else{
            String timeSpend = countTimeSpend(accesses, request.getWhen());

            sendTimeSpend(timeSpend);
            }
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing JSON request: " + e.getMessage());
        }
    }

    private String countTimeSpend(Iterable<Access> accesses, LocalDate date) {
        Access employeeAccess = null;
        Access lastAccess = null;
        long totalSeconds = 0;

        Timestamp end = date.isEqual(LocalDate.now())
                ? Timestamp.valueOf(LocalDateTime.now())
                : Timestamp.valueOf(date.atTime(LocalTime.MAX));
        Timestamp start = Timestamp.valueOf(date.atStartOfDay());
        for (Access access : accesses) {
            employeeAccess = access;
            if(access.getDirection().equals("IN")) {
                lastAccess = access;
            }else{
                if(lastAccess != null) {
                    totalSeconds += Duration.between(
                            lastAccess.getTimestamp().toLocalDateTime(),
                            access.getTimestamp().toLocalDateTime()
                    ).getSeconds();
                    lastAccess = null;
                }else{
                    totalSeconds += Duration.between(
                            start.toLocalDateTime(),
                            access.getTimestamp().toLocalDateTime()
                    ).getSeconds();
                }
            }
        }
        if (lastAccess != null) {
            totalSeconds += Duration.between(
                    lastAccess.getTimestamp().toLocalDateTime(),
                    end.toLocalDateTime()
            ).getSeconds();
        }

        // Convert seconds to hours and minutes
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;

        String timeSpend  = "Employee with cardId " + employeeAccess.getCard() + " spent " + hours
                + " hours and " + minutes + " minutes at work on " + date;

        return timeSpend;
    }

    private Request requestFromJson(String jsonRequest) throws JsonProcessingException {
       Request request = new Request();
        String cleanedString = jsonRequest.replace("Request{", "").replace("}", "");
        String[] parts = cleanedString.split(",");
        LocalDate when = null;
        int cardId = 0;

        // Parse each key-value pair
        for (String pair : parts) {
            String[] keyValue = pair.split("=");
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            if ("when".equals(key)) {
                when = LocalDate.parse(value);
            } else if ("cardId".equals(key)) {
                cardId = Integer.parseInt(value);
            }
        }

        request.setWhen(when);
        request.setCardId(cardId);
       return request;
    }
}
