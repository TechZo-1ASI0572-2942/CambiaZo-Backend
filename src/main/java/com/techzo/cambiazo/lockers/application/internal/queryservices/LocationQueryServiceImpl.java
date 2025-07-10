package com.techzo.cambiazo.lockers.application.internal.queryservices;

import com.techzo.cambiazo.lockers.domain.model.entities.Location;
import com.techzo.cambiazo.lockers.domain.model.queries.GetAllLocationsQuery;
import com.techzo.cambiazo.lockers.domain.model.queries.GetLocationByIdQuery;
import com.techzo.cambiazo.lockers.domain.services.ILocationQueryService;
import com.techzo.cambiazo.lockers.infrastructure.persistence.jpa.ILocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationQueryServiceImpl implements ILocationQueryService {

    private final ILocationRepository locationRepository;

    public LocationQueryServiceImpl(ILocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Optional<Location> handle(GetLocationByIdQuery query) {
        return locationRepository.findById(query.id());
    }

    @Override
    public List<Location> handle(GetAllLocationsQuery query) {
        return locationRepository.findAll();
    }
}
