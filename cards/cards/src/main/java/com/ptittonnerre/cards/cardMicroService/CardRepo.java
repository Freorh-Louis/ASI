package com.ptittonnerre.cards.cardMicroService;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CardRepo extends JpaRepository<Card, String> {
    List<Card> findByUserId(String userId);
}
