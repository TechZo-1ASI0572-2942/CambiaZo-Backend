package com.techzo.cambiazo.lockers.domain.services;

import com.techzo.cambiazo.lockers.domain.model.commands.CreateLocationCommand;
import com.techzo.cambiazo.lockers.domain.model.entities.Location;

import java.util.Optional;

public interface ILocationCommandService {

    Optional<Location> handle(CreateLocationCommand command);
}
