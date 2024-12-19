package sk.upjs.kopr.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name = "card")
public class Card {

    @Id
    private int cardId;

    // Getters and Setters
    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardId=" + cardId +
                '}';
    }
}