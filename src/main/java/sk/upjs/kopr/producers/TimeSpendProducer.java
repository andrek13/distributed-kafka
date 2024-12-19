package sk.upjs.kopr.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sk.upjs.kopr.kopr_rabbit.Request;

@Component
public class TimeSpendProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 5000)
    void sendTimeSpend(){
        try {
            Request request = Request.randomRequest();
            System.out.println("Sending request: " + request);
            String jsonRequest = requestToJson(request);
            kafkaTemplate.send("timespend", jsonRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String requestToJson(Request request){
        String jsonRequest = "Request{when=" + request.getWhen() + ", cardId=" + request.getCardId() + "}";
        return jsonRequest;
    }

    @KafkaListener(topics = "requestResponses")
    void requestResponses(String response){
        System.out.println("Received response: " + response);
    }

}
