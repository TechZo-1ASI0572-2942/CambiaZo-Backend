package com.techzo.cambiazo.lockers.interfaces.transform;

import com.techzo.cambiazo.lockers.domain.model.entities.Location;
import com.techzo.cambiazo.lockers.interfaces.resources.LocationResource;

public class LocationResourceFromEntityAssembler {
    public static LocationResource toResourceFromEntity(Location entity) {
        return new LocationResource(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getDistrictId()
        );
    }
}
