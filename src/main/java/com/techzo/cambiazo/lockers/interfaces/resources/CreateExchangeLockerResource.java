package com.techzo.cambiazo.lockers.interfaces.resources;

public record CreateExchangeLockerResource(
    String pinDeposit,
    String pinRetrieve,
    String state,
    Long userDepositId,
    Long userRetrieveId,
    Long lockerId,
    Long exchangeId
) {
    public CreateExchangeLockerResource {
        if (pinDeposit == null || pinDeposit.isBlank()) {
            throw new IllegalArgumentException("Pin deposit is required");
        }
        if (pinRetrieve == null || pinRetrieve.isBlank()) {
            throw new IllegalArgumentException("Pin retrieve is required");
        }
        if (userDepositId == null) {
            throw new IllegalArgumentException("User deposit ID is required");
        }
        if (userRetrieveId == null) {
            throw new IllegalArgumentException("User retrieve ID is required");
        }
        if (lockerId == null) {
            throw new IllegalArgumentException("Locker ID is required");
        }
        if (state == null || state.isBlank()) {
            throw new IllegalArgumentException("Locker state is required");
        }
        if (exchangeId == null) {
            throw new IllegalArgumentException("Exchange ID is required");
        }
    }
}
