package sk.upjs.kopr.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;

@Entity
@Table(name = "access")
public class Access {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int cardId;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @Column(nullable = false)
    private int location;

    @Column(nullable = false, length = 3)
    private String direction;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCard() {
        return cardId;
    }

    public void setCard(int cardId) {
        this.cardId = cardId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Access{" +
                "id=" + id +
                ", card=" + cardId +
                ", timestamp=" + timestamp +
                ", location=" + location +
                ", direction='" + direction + '\'' +
                '}';
    }

    // Method to create a random Access instance
    public static Access randomAccess() {
        Access access = new Access();
        access.setCard((int)(new Random().nextDouble() * 10)); // Random card between 0 and 10
        access.setTimestamp(Timestamp.from(Instant.now()));
        access.setLocation((int)(new Random().nextDouble() * 10)); // Random location between 0 and 1000
        access.setDirection(new Random().nextBoolean() ? "IN" : "OUT");
        return access;
    }
}