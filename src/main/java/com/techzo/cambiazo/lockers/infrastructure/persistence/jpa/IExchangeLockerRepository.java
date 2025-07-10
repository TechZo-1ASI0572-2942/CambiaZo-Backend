package com.techzo.cambiazo.lockers.infrastructure.persistence.jpa;

import com.techzo.cambiazo.exchanges.domain.model.entities.Exchange;
import com.techzo.cambiazo.lockers.domain.model.entities.ExchangeLocker;
import com.techzo.cambiazo.lockers.domain.model.entities.Locker;
import com.techzo.cambiazo.lockers.domain.model.valueObjects.LockerExchangeState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IExchangeLockerRepository extends JpaRepository<ExchangeLocker, Long> {

    List<ExchangeLocker> findExchangeLockersByExchangeId(Exchange exchangeId);

    Optional<ExchangeLocker> findByExchangeIdAndLockerId(Exchange exchangeId, Locker lockerId);

    Optional<ExchangeLocker> findByExchangeIdAndUserDepositId(Exchange exchangeId, Long userDepositId);

    Optional<ExchangeLocker> findByExchangeIdAndUserRetrieveId(Exchange exchangeId, Long userRetrieveId);

    List<ExchangeLocker> findExchangeLockersByExchangeIdAndState(Exchange exchangeId, LockerExchangeState state);
}
