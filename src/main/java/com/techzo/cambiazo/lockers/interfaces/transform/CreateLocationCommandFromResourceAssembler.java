package com.techzo.cambiazo.lockers.interfaces.transform;

import com.techzo.cambiazo.lockers.domain.model.commands.CreateLocationCommand;
import com.techzo.cambiazo.lockers.interfaces.resources.CreateLocationResource;

public class CreateLocationCommandFromResourceAssembler {
    public static CreateLocationCommand toCommandFromResource(CreateLocationResource resource) {
        return new CreateLocationCommand(resource.name(), resource.address(), resource.districtId());
    }
}
