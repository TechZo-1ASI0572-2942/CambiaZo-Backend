package com.techzo.cambiazo.lockers.domain.model.commands;

public record UpdateExchangeLockerCommand(
        String lockerId,
        Long exchangeId,
        String state,
        String stateExchange
) {
}
