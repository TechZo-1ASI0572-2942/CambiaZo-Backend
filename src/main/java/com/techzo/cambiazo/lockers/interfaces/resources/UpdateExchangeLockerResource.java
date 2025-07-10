package com.techzo.cambiazo.lockers.interfaces.resources;

public record UpdateExchangeLockerResource(
        Long exchangeId,
        String state,
        String stateExchange
) {
}
