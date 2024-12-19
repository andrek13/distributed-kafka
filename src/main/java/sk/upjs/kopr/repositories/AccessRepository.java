package sk.upjs.kopr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.upjs.kopr.entities.Access;


import java.sql.Timestamp;
import java.util.List;

@Repository
public interface AccessRepository extends JpaRepository<Access, Long> {
    // Find all access entries for a specific card
    List<Access> findByCardId(int cardId);

    // Find all access entries for a specific direction
    List<Access> findByDirection(String direction);

    // Find the latest access entry for a specific card
    Access findTopByCardIdOrderByTimestampDesc(int card);
    //List<Access> findAllByCardIdAndTimestampBetween(int cardId, Timestamp from, Timestamp to);
    @Query("SELECT a FROM Access a WHERE a.cardId = :cardId AND a.timestamp >= :start AND a.timestamp <= :end")
    List<Access> findAccessBetween(@Param("cardId") int cardId, @Param("start") Timestamp start, @Param("end") Timestamp end);
}