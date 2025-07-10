package com.techzo.cambiazo.lockers.application.internal.queryservices;

import com.techzo.cambiazo.lockers.domain.model.entities.Location;
import com.techzo.cambiazo.lockers.domain.model.entities.Locker;
import com.techzo.cambiazo.lockers.domain.model.queries.GetAllLockersByLocationId;
import com.techzo.cambiazo.lockers.domain.model.queries.GetAllLockersQuery;
import com.techzo.cambiazo.lockers.domain.model.queries.GetLockerByIdQuery;
import com.techzo.cambiazo.lockers.domain.services.ILockerQueryService;
import com.techzo.cambiazo.lockers.infrastructure.persistence.jpa.ILocationRepository;
import com.techzo.cambiazo.lockers.infrastructure.persistence.jpa.ILockerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LockerQueryServiceImpl implements ILockerQueryService {

    private final ILocationRepository locationRepository;
    private final ILockerRepository lockerRepository;

    public LockerQueryServiceImpl(ILocationRepository locationRepository, ILockerRepository lockerRepository) {
        this.locationRepository = locationRepository;
        this.lockerRepository = lockerRepository;
    }

    @Override
    public Optional<Locker> handle(GetLockerByIdQuery query) {
        return lockerRepository.findById(query.id());
    }

    @Override
    public List<Locker> handle(GetAllLockersByLocationId query) {
        Location location = locationRepository.findById(query.locationId())
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + query.locationId()));
        return lockerRepository.findLockersByLocationId(location);
    }

    @Override
    public List<Locker> handle(GetAllLockersQuery query) {
        var lockers = lockerRepository.findAll();
        return lockers.isEmpty() ? List.of() : lockers;
    }
}
