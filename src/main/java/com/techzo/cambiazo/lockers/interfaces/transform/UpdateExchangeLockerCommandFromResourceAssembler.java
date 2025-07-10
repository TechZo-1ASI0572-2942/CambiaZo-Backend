package com.techzo.cambiazo.lockers.interfaces.transform;

import com.techzo.cambiazo.lockers.domain.model.commands.UpdateExchangeLockerCommand;
import com.techzo.cambiazo.lockers.interfaces.resources.UpdateExchangeLockerResource;

public class UpdateExchangeLockerCommandFromResourceAssembler {
    public static UpdateExchangeLockerCommand toCommandFromResource(String lockerId,UpdateExchangeLockerResource resource) {
        return new UpdateExchangeLockerCommand(
                lockerId,
                resource.exchangeId(),
                resource.state(),
                resource.stateExchange()
        );
    }
}
