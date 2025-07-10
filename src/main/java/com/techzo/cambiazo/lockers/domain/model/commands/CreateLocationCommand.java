package com.techzo.cambiazo.lockers.domain.model.commands;

public record CreateLocationCommand(String name, String address, Long districtId) {

    public CreateLocationCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (districtId == null) {
            throw new IllegalArgumentException("District ID is required");
        }
    }
}
