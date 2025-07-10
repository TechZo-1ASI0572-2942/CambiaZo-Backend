package com.techzo.cambiazo.lockers.interfaces.transform;

import com.techzo.cambiazo.lockers.domain.model.commands.CreateLockerCommand;
import com.techzo.cambiazo.lockers.interfaces.resources.CreateLockerResource;

public class CreateLockerCommandFromResourceAssembler {
    public static CreateLockerCommand toCommandFromResource(CreateLockerResource resource) {
        return new CreateLockerCommand(resource.lockerId(), resource.lockerState(), resource.locationId());
    }
}
