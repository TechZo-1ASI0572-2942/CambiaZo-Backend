package com.techzo.cambiazo.lockers.domain.services;

import com.techzo.cambiazo.lockers.domain.model.entities.Location;
import com.techzo.cambiazo.lockers.domain.model.queries.GetAllLocationsQuery;
import com.techzo.cambiazo.lockers.domain.model.queries.GetLocationByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ILocationQueryService {

    Optional<Location> handle(GetLocationByIdQuery query);

    List<Location>handle(GetAllLocationsQuery query);
}
