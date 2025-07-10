package com.techzo.cambiazo.lockers.interfaces.resources;

public record LocationResource(Long id, String name, String address, Long districtId) {
    public LocationResource {
        if (name == null || address == null || districtId == null) {
            throw new IllegalArgumentException("name, address, and districtId cannot be null");
        }
    }

    public LocationResource(Long id, String name, String address) {
        this(id, name, address, null);
    }
}
