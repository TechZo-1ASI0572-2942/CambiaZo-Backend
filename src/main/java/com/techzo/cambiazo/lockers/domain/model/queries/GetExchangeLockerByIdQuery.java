package com.techzo.cambiazo.lockers.domain.model.queries;

public record GetExchangeLockerByIdQuery(Long lockerId) {

    public GetExchangeLockerByIdQuery {
        if (lockerId == null || lockerId <= 0) {
            throw new IllegalArgumentException("Locker ID must be a positive number.");
        }
    }
}
