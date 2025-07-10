package com.techzo.cambiazo.lockers.domain.services;

import com.techzo.cambiazo.lockers.domain.model.dtos.ModifiedExchangeLocker;
import com.techzo.cambiazo.lockers.domain.model.entities.ExchangeLocker;
import com.techzo.cambiazo.lockers.domain.model.queries.GetAllExchangeLockersByExchangeIdQuery;
import com.techzo.cambiazo.lockers.domain.model.queries.GetAllExchangeLockersQuery;
import com.techzo.cambiazo.lockers.domain.model.queries.GetExchangeLockerByExchangeAndUserIdQuery;
import com.techzo.cambiazo.lockers.domain.model.queries.GetExchangeLockerByIdQuery;

import java.util.List;
import java.util.Optional;

public interface IExchangeLockerQueryService {
    Optional<ExchangeLocker>handle(GetExchangeLockerByIdQuery query);

    List<ExchangeLocker>handle(GetAllExchangeLockersByExchangeIdQuery query);

    List<ExchangeLocker>handle(GetAllExchangeLockersQuery query);

    Optional<ModifiedExchangeLocker> handle(GetExchangeLockerByExchangeAndUserIdQuery query);

}
