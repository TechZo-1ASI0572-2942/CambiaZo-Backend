package com.techzo.cambiazo.lockers.interfaces;

import com.techzo.cambiazo.lockers.domain.model.queries.GetAllLockersByLocationId;
import com.techzo.cambiazo.lockers.domain.model.queries.GetAllLockersQuery;
import com.techzo.cambiazo.lockers.domain.model.queries.GetLockerByIdQuery;
import com.techzo.cambiazo.lockers.domain.services.ILockerCommandService;
import com.techzo.cambiazo.lockers.domain.services.ILockerQueryService;
import com.techzo.cambiazo.lockers.interfaces.resources.CreateLockerResource;
import com.techzo.cambiazo.lockers.interfaces.resources.LockerResource;
import com.techzo.cambiazo.lockers.interfaces.transform.CreateLockerCommandFromResourceAssembler;
import com.techzo.cambiazo.lockers.interfaces.transform.LockerResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("api/v2/lockers")
@Tag(name= "Lockers", description = "Lockers Management Endpoints")
public class LockerController {

    private final ILockerQueryService lockerQueryService;
    private final ILockerCommandService lockerCommandService;

    public LockerController(ILockerQueryService lockerQueryService, ILockerCommandService lockerCommandService) {
        this.lockerQueryService = lockerQueryService;
        this.lockerCommandService = lockerCommandService;
    }

    @Operation(summary = "Create a new Locker", description = "Create a new Locker with the input data.")
    @PostMapping
    public ResponseEntity<LockerResource> createLocker(@RequestBody CreateLockerResource resource) {
        try {
            var createLockerCommand = CreateLockerCommandFromResourceAssembler.toCommandFromResource(resource);
            var locker = lockerCommandService.handle(createLockerCommand);
            var lockerResource = LockerResourceFromEntityAssembler.toResourceFromEntity(locker.get());
            return ResponseEntity.status(CREATED).body(lockerResource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LockerResource> getLockerById(@PathVariable Long id) {
        try {
            var getLockerByIdQuery = new GetLockerByIdQuery(id);
            var locker = lockerQueryService.handle(getLockerByIdQuery);
            var lockerResource = LockerResourceFromEntityAssembler.toResourceFromEntity(locker.get());
            return ResponseEntity.ok(lockerResource);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<LockerResource>> getAllLockers() {
        try {
            var getAllLockersQuery = new GetAllLockersQuery();
            var lockers = lockerQueryService.handle(getAllLockersQuery);
            var lockerResources = lockers.stream()
                    .map(LockerResourceFromEntityAssembler::toResourceFromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(lockerResources);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<LockerResource>> getLockersByLocationId(@PathVariable Long locationId) {
        try {
            var getAllLockersByLocationIdQuery = new GetAllLockersByLocationId(locationId);
            var lockers = lockerQueryService.handle(getAllLockersByLocationIdQuery);
            var lockerResources = lockers.stream()
                    .map(LockerResourceFromEntityAssembler::toResourceFromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(lockerResources);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
