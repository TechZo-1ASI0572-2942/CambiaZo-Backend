package com.techzo.cambiazo.lockers.interfaces.transform;

import com.techzo.cambiazo.lockers.domain.model.commands.CreateExchangeLockerCommand;
import com.techzo.cambiazo.lockers.interfaces.resources.CreateExchangeLockerResource;

public class CreateExchangeLockerCommandFromResourceAssembler {
    public static CreateExchangeLockerCommand toCommandFromResource(CreateExchangeLockerResource resource) {
        return new CreateExchangeLockerCommand(
                resource.pinDeposit(),
                resource.pinRetrieve(),
                resource.state(),
                resource.userDepositId(),
                resource.userRetrieveId(),
                resource.lockerId(),
                resource.exchangeId());
    }
}
