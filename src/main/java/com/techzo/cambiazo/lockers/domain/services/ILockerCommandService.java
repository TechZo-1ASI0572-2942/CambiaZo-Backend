package com.techzo.cambiazo.lockers.domain.services;

import com.techzo.cambiazo.lockers.domain.model.commands.CreateLockerCommand;
import com.techzo.cambiazo.lockers.domain.model.entities.Locker;

import java.util.Optional;

public interface ILockerCommandService {
    Optional<Locker>handle(CreateLockerCommand command);
}
