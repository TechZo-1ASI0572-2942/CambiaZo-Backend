package com.techzo.cambiazo.lockers.interfaces.transform;

import com.techzo.cambiazo.lockers.domain.model.entities.Locker;
import com.techzo.cambiazo.lockers.interfaces.resources.LockerResource;

public class LockerResourceFromEntityAssembler {
    public static LockerResource toResourceFromEntity(Locker entity) {
        return new LockerResource(
                entity.getId(),
                entity.getLockerId(),
                entity.getLockerState(),
                entity.getLocationId()
        );
    }
}
