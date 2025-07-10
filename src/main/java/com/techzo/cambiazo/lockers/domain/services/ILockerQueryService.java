package com.techzo.cambiazo.lockers.domain.services;

import com.techzo.cambiazo.lockers.domain.model.entities.Locker;
import com.techzo.cambiazo.lockers.domain.model.queries.GetAllLockersByLocationId;
import com.techzo.cambiazo.lockers.domain.model.queries.GetAllLockersQuery;
import com.techzo.cambiazo.lockers.domain.model.queries.GetLockerByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ILockerQueryService {

    Optional<Locker> handle(GetLockerByIdQuery query);

    List<Locker>handle(GetAllLockersByLocationId query);

    List<Locker>handle(GetAllLockersQuery query);
}
