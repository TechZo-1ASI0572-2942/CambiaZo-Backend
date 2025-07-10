package com.techzo.cambiazo.lockers.domain.model.valueObjects;

public enum LockerState {
    AVAILABLE,
    IN_USE,
    OUT_OF_SERVICE;

    public static LockerState fromString(String state) {
        try {
            return LockerState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            return OUT_OF_SERVICE;
        }
    }
}
