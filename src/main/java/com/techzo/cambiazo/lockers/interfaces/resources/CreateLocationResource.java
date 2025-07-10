package com.techzo.cambiazo.lockers.interfaces.resources;

public record CreateLocationResource(String name, String address, Long districtId) {

    public CreateLocationResource {
        if (name == null || address == null || districtId == null) {
            throw new IllegalArgumentException("name, address, and districtId cannot be null");
        }
    }
}
