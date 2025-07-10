package com.techzo.cambiazo.lockers.domain.model.commands;

public record CreateLockerCommand(String lockerId, String state, Long locationId) {

    public CreateLockerCommand {
        if (lockerId == null || lockerId.isBlank()) {
            throw new IllegalArgumentException("Locker ID cannot be null or blank");
        }
        if (state == null || state.isBlank()) {
            throw new IllegalArgumentException("State cannot be null or blank");
        }
        if (locationId == null) {
            throw new IllegalArgumentException("Location ID cannot be null");
        }
    }
}
