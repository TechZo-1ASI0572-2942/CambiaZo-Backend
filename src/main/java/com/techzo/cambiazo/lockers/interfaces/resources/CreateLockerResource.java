package com.techzo.cambiazo.lockers.interfaces.resources;

public record CreateLockerResource(String lockerId, String lockerState, Long locationId) {

    public CreateLockerResource {
        if (lockerId == null || lockerState == null || locationId == null) {
            throw new IllegalArgumentException("lockerId, lockerState, and locationId cannot be null");
        }

    }

}
