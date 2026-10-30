package com.ptittonnerre.market.marketMicroService;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarketRepo extends JpaRepository<Market, Long> {
    List<Market> findByTransactionType(TransactionType transactionType);
    Optional<Market> findByCardIdAndTransactionType(String cardId, TransactionType transactionType);


}
