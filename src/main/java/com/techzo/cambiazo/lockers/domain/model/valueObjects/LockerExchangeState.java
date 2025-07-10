package com.techzo.cambiazo.lockers.domain.model.valueObjects;

public enum LockerExchangeState {
    EMPTY,
    FULL,
    DELIVERED;

    public static LockerExchangeState fromString(String state) {
        try {
            return LockerExchangeState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            return EMPTY;
        }
    }
}
