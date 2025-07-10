package com.techzo.cambiazo.lockers.application.internal.commandservices;

import com.techzo.cambiazo.lockers.domain.model.commands.CreateLockerCommand;
import com.techzo.cambiazo.lockers.domain.model.entities.Location;
import com.techzo.cambiazo.lockers.domain.model.entities.Locker;
import com.techzo.cambiazo.lockers.domain.services.ILockerCommandService;
import com.techzo.cambiazo.lockers.infrastructure.persistence.jpa.ILocationRepository;
import com.techzo.cambiazo.lockers.infrastructure.persistence.jpa.ILockerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LockerCommandServiceImpl implements ILockerCommandService {

    private final ILocationRepository locationRepository;
    private final ILockerRepository lockerRepository;

    public LockerCommandServiceImpl(ILocationRepository locationRepository, ILockerRepository lockerRepository) {
        this.locationRepository = locationRepository;
        this.lockerRepository = lockerRepository;
    }

    @Override
    public Optional<Locker> handle(CreateLockerCommand command) {
        Location location = locationRepository.findById(command.locationId())
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + command.locationId()));

        var locker = new Locker(command, location);
        lockerRepository.save(locker);
        return Optional.of(locker);
    }
}
