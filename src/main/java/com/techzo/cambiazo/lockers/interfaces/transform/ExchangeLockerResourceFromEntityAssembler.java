package com.techzo.cambiazo.lockers.interfaces.transform;

import com.techzo.cambiazo.exchanges.interfaces.rest.resources.ExchangeResource;
import com.techzo.cambiazo.lockers.domain.model.entities.ExchangeLocker;
import com.techzo.cambiazo.lockers.interfaces.resources.ExchangeLockerResource;

public class ExchangeLockerResourceFromEntityAssembler {
    public static ExchangeLockerResource toResourceFromEntity(ExchangeLocker entity) {
        return new ExchangeLockerResource(
            entity.getId(),
            entity.getPinDeposit(),
            entity.getPinRetrieve(),
                entity.getState().name(),
                entity.getUserDepositId(),
            entity.getUserRetrieveId(),
                entity.getLockerId().getId(),
            entity.getExchangeId().getId()

        );
    }
}
