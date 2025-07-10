package com.techzo.cambiazo.exchanges.domain.model.commands;

public record CreateExchangeCommand(Long productOwnId, Long productChangeId, String status, Long locationId) {

        public CreateExchangeCommand {
            if (productOwnId == null) {
                throw new IllegalArgumentException("Product Own ID is required");
            }
            if (productChangeId == null) {
                throw new IllegalArgumentException("Product Change ID is required");
            }
            if (status == null || status.isBlank()) {
                throw new IllegalArgumentException("Status is required");
            }
            if (locationId == null) {
                throw new IllegalArgumentException("Location ID is required");
            }
        }
}
