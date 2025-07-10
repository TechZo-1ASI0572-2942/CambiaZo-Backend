package com.techzo.cambiazo.lockers.interfaces;

import com.techzo.cambiazo.lockers.domain.model.dtos.ModifiedExchangeLocker;
import com.techzo.cambiazo.lockers.domain.model.queries.GetAllExchangeLockersByExchangeIdQuery;
import com.techzo.cambiazo.lockers.domain.model.queries.GetAllExchangeLockersQuery;
import com.techzo.cambiazo.lockers.domain.model.queries.GetExchangeLockerByExchangeAndUserIdQuery;
import com.techzo.cambiazo.lockers.domain.model.queries.GetExchangeLockerByIdQuery;
import com.techzo.cambiazo.lockers.domain.services.IExchangeLockerCommandService;
import com.techzo.cambiazo.lockers.domain.services.IExchangeLockerQueryService;
import com.techzo.cambiazo.lockers.interfaces.resources.CreateExchangeLockerResource;
import com.techzo.cambiazo.lockers.interfaces.resources.ExchangeLockerResource;
import com.techzo.cambiazo.lockers.interfaces.resources.UpdateExchangeLockerResource;
import com.techzo.cambiazo.lockers.interfaces.transform.CreateExchangeLockerCommandFromResourceAssembler;
import com.techzo.cambiazo.lockers.interfaces.transform.ExchangeLockerResourceFromEntityAssembler;
import com.techzo.cambiazo.lockers.interfaces.transform.UpdateExchangeLockerCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("api/v2/exchange-lockers")
@Tag(name = "Exchange Locker", description = "Exchange Locker Management Endpoints")
public class ExchangeLockerController {

    private final IExchangeLockerQueryService exchangeLockerQueryService;
    private final IExchangeLockerCommandService exchangeLockerCommandService;

    public ExchangeLockerController(IExchangeLockerQueryService exchangeLockerQueryService,
                                    IExchangeLockerCommandService exchangeLockerCommandService) {
        this.exchangeLockerQueryService = exchangeLockerQueryService;
        this.exchangeLockerCommandService = exchangeLockerCommandService;
    }

    @Operation(summary = "Create a new Exchange Locker", description = "Create a new Exchange Locker with the input data.")
    @PostMapping
    public ResponseEntity<ExchangeLockerResource> createExchangeLocker(@RequestBody CreateExchangeLockerResource resource) {
        try{
            var createExchangeLockerCommand = CreateExchangeLockerCommandFromResourceAssembler.toCommandFromResource(resource);
            var exchangeLocker = exchangeLockerCommandService.handle(createExchangeLockerCommand);
            var exchangeLockerResource = ExchangeLockerResourceFromEntityAssembler.toResourceFromEntity(exchangeLocker.get());
            return ResponseEntity.status(CREATED).body(exchangeLockerResource);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExchangeLockerResource> getExchangeLockerById(@PathVariable Long id) {
        try {
            var getExchangeLockerByIdQuery = new GetExchangeLockerByIdQuery(id);
            var exchangeLocker = exchangeLockerQueryService.handle(getExchangeLockerByIdQuery);
            var exchangeLockerResource = ExchangeLockerResourceFromEntityAssembler.toResourceFromEntity(exchangeLocker.get());
            return ResponseEntity.ok(exchangeLockerResource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ExchangeLockerResource>> getAllExchangeLockers() {
        try {
            var exchangeLockers = exchangeLockerQueryService.handle(new GetAllExchangeLockersQuery());
            var exchangeLockerResources = exchangeLockers.stream()
                    .map(ExchangeLockerResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
            return ResponseEntity.ok(exchangeLockerResources);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/exchange/{exchangeId}")
    public ResponseEntity<List<ExchangeLockerResource>> getAllExchangeLockersByExchangeId(@PathVariable Long exchangeId) {
        try {
            var exchangeLockers = exchangeLockerQueryService.handle(new GetAllExchangeLockersByExchangeIdQuery(exchangeId));
            var exchangeLockerResources = exchangeLockers.stream()
                    .map(ExchangeLockerResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
            return ResponseEntity.ok(exchangeLockerResources);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/exchange/{exchangeId}/{userId}")
    public ResponseEntity<ModifiedExchangeLocker>getExchangeLockerByExchangeAndUserId(@PathVariable Long exchangeId, @PathVariable Long userId) {
        try{
            var getExchangeLockerByExchangeAndUserIdQuery = new GetExchangeLockerByExchangeAndUserIdQuery(exchangeId, userId);
            var exchangeLocker = exchangeLockerQueryService.handle(getExchangeLockerByExchangeAndUserIdQuery);
            return ResponseEntity.ok(exchangeLocker.get());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/{lockerId}")
    public ResponseEntity<ExchangeLockerResource> updateExchangeLocker(@PathVariable String lockerId, @RequestBody UpdateExchangeLockerResource resource) {
        try {
            var updateExchangeLockerCommand = UpdateExchangeLockerCommandFromResourceAssembler.toCommandFromResource(lockerId,resource);
            var exchangeLocker = exchangeLockerCommandService.handle(updateExchangeLockerCommand);
            var exchangeLockerResource = ExchangeLockerResourceFromEntityAssembler.toResourceFromEntity(exchangeLocker.get());
            return ResponseEntity.ok(exchangeLockerResource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
