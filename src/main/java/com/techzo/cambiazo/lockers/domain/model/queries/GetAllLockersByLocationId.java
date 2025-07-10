package com.techzo.cambiazo.lockers.domain.model.queries;

public record GetAllLockersByLocationId(Long locationId) {

    public GetAllLockersByLocationId {
        if (locationId == null || locationId <= 0) {
            throw new IllegalArgumentException("Location ID must be a positive number.");
        }
    }
}
