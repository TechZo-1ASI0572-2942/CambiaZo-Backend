package com.techzo.cambiazo.lockers.application.internal.commandservices;

import com.techzo.cambiazo.exchanges.domain.model.entities.District;
import com.techzo.cambiazo.exchanges.infrastructure.persistence.jpa.IDistrictRepository;
import com.techzo.cambiazo.lockers.domain.model.commands.CreateLocationCommand;
import com.techzo.cambiazo.lockers.domain.model.entities.Location;
import com.techzo.cambiazo.lockers.domain.services.ILocationCommandService;
import com.techzo.cambiazo.lockers.infrastructure.persistence.jpa.ILocationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationCommandServiceImpl implements ILocationCommandService {

    private final IDistrictRepository districtRepository;
    private final ILocationRepository locationRepository;

    public LocationCommandServiceImpl(IDistrictRepository districtRepository, ILocationRepository locationRepository) {
        this.districtRepository = districtRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public Optional<Location> handle(CreateLocationCommand command) {
        District district = districtRepository.findById(command.districtId())
                .orElseThrow(() -> new IllegalArgumentException("District with same id already exists"));

        var location = new Location(command, district);
        locationRepository.save(location);
        return Optional.of(location);

    }
}
