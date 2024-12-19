package sk.upjs.kopr.kopr_rabbit;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Random;

public class Request implements Serializable {

    private static final long serialVersionUID = 1L;
    private LocalDate when;
    private int cardId;

    public LocalDate getWhen() {
        return when;
    }

    public void setWhen(LocalDate when) {
        this.when = when;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    @Override
    public String toString() {
        return "Request{" +
                "when=" + when +
                ", cardId=" + cardId +
                '}';
    }

    public static Request randomRequest(){
        Request request = new Request();
        request.setWhen(new Random().nextBoolean() ?
                LocalDate.now() :
                LocalDate.now().minusDays(1));

//        request.setWhen(LocalDate.now());
        request.setCardId((int)(new Random().nextDouble() * 3) + 1);
        return request;
    }
}
