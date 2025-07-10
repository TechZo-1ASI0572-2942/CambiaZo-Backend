package com.techzo.cambiazo.lockers.interfaces.resources;

public record LockerResource(Long id, String lockerId, String lockerState, Long locationId) {

    public LockerResource {
        if (lockerId == null || lockerState == null || locationId == null) {
            throw new IllegalArgumentException("lockerId, lockerState, and locationId cannot be null");
        }
    }

    public LockerResource(Long id, String lockerId, String lockerState) {
        this(id, lockerId, lockerState, null);
    }
}
