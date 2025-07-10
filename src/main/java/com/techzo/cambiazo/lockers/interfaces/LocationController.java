package com.techzo.cambiazo.lockers.interfaces;


import com.techzo.cambiazo.lockers.domain.model.queries.GetAllLocationsQuery;
import com.techzo.cambiazo.lockers.domain.model.queries.GetLocationByIdQuery;
import com.techzo.cambiazo.lockers.domain.services.ILocationCommandService;
import com.techzo.cambiazo.lockers.domain.services.ILocationQueryService;
import com.techzo.cambiazo.lockers.interfaces.resources.CreateLocationResource;
import com.techzo.cambiazo.lockers.interfaces.resources.LocationResource;
import com.techzo.cambiazo.lockers.interfaces.transform.CreateLocationCommandFromResourceAssembler;
import com.techzo.cambiazo.lockers.interfaces.transform.LocationResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("api/v2/locations")
@Tag(name = "Locations", description = "Locations Management Endpoints")
public class LocationController {

    private final ILocationQueryService locationQueryService;
    private final ILocationCommandService locationCommandService;

    public LocationController(ILocationQueryService locationQueryService, ILocationCommandService locationCommandService) {
        this.locationQueryService = locationQueryService;
        this.locationCommandService = locationCommandService;
    }

    @Operation(summary = "Create a new Location", description = "Create a new Location with the input data.")
    @PostMapping
    public ResponseEntity<LocationResource> createLocation(@RequestBody CreateLocationResource resource) {
        try {
            var createLocationCommand = CreateLocationCommandFromResourceAssembler.toCommandFromResource(resource);
            var location = locationCommandService.handle(createLocationCommand);
            var locationResource = LocationResourceFromEntityAssembler.toResourceFromEntity(location.get());
            return ResponseEntity.status(CREATED).body(locationResource);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResource> getLocationById(@PathVariable Long id) {
        try {
            var getLocationByIdQuery = new GetLocationByIdQuery(id);
            var location = locationQueryService.handle(getLocationByIdQuery);
            var locationResource = LocationResourceFromEntityAssembler.toResourceFromEntity(location.get());
            return ResponseEntity.ok(locationResource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<LocationResource>> getAllLocations() {
        try {
            var getAllLocationsQuery = new GetAllLocationsQuery();
            var locations = locationQueryService.handle(getAllLocationsQuery);
            var locationResources = locations.stream().map(LocationResourceFromEntityAssembler::toResourceFromEntity).toList();
            return ResponseEntity.ok(locationResources);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
