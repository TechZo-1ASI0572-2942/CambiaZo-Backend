package com.techzo.cambiazo.lockers.application.internal.queryservices;

import com.techzo.cambiazo.exchanges.domain.model.entities.Exchange;
import com.techzo.cambiazo.exchanges.infrastructure.persistence.jpa.IExchangeRepository;
import com.techzo.cambiazo.iam.domain.model.aggregates.User;
import com.techzo.cambiazo.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.techzo.cambiazo.lockers.domain.model.dtos.ModifiedExchangeLocker;
import com.techzo.cambiazo.lockers.domain.model.entities.ExchangeLocker;
import com.techzo.cambiazo.lockers.domain.model.entities.Location;
import com.techzo.cambiazo.lockers.domain.model.queries.GetAllExchangeLockersByExchangeIdQuery;
import com.techzo.cambiazo.lockers.domain.model.queries.GetAllExchangeLockersQuery;
import com.techzo.cambiazo.lockers.domain.model.queries.GetExchangeLockerByExchangeAndUserIdQuery;
import com.techzo.cambiazo.lockers.domain.model.queries.GetExchangeLockerByIdQuery;
import com.techzo.cambiazo.lockers.domain.services.IExchangeLockerQueryService;
import com.techzo.cambiazo.lockers.infrastructure.persistence.jpa.IExchangeLockerRepository;
import com.techzo.cambiazo.lockers.infrastructure.persistence.jpa.ILocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExchangeLockerQueryServiceImpl implements IExchangeLockerQueryService {

    private final IExchangeLockerRepository exchangeLockerRepository;
    private final IExchangeRepository exchangeRepository;
    private final UserRepository userRepository;
    private final ILocationRepository locationRepository;

    public ExchangeLockerQueryServiceImpl(IExchangeLockerRepository exchangeLockerRepository,
                                           IExchangeRepository exchangeRepository,
                                          UserRepository userRepository,
                                          ILocationRepository locationRepository) {
        this.exchangeLockerRepository = exchangeLockerRepository;
        this.exchangeRepository = exchangeRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public Optional<ModifiedExchangeLocker> handle(GetExchangeLockerByExchangeAndUserIdQuery query) {
        Exchange exchange = exchangeRepository.findById(query.exchangeId())
                .orElseThrow(() -> new IllegalArgumentException("Exchange not found with id: " + query.exchangeId()));
        User user = userRepository.findById(query.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + query.userId()));

        ExchangeLocker exchangeLockerForDeposit = exchangeLockerRepository.findByExchangeIdAndUserDepositId(exchange,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Exchange Locker not found for exchange id: " + query.exchangeId() + " and user id: " + query.userId()));

        ExchangeLocker exchangeLockerForRetrieve = exchangeLockerRepository.findByExchangeIdAndUserRetrieveId(exchange,user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Exchange Locker not found for exchange id: " + query.exchangeId() + " and user id: " + query.userId()));

        Location location = locationRepository.findById(exchangeLockerForDeposit.getLockerId().getLocationId())
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + exchangeLockerForDeposit.getLockerId().getLocationId()));

        ModifiedExchangeLocker modifiedExchangeLocker = new ModifiedExchangeLocker(
                exchangeLockerForDeposit.getPinDeposit(),
                exchangeLockerForRetrieve.getPinRetrieve(),
                exchangeLockerForDeposit.getLockerId().getLockerId(),
                exchangeLockerForRetrieve.getLockerId().getLockerId(),
                location,
                exchangeLockerForDeposit.getState().name(),
                exchangeLockerForRetrieve.getState().name()

        );

        return Optional.of(modifiedExchangeLocker);

    }

    @Override
    public Optional<ExchangeLocker> handle(GetExchangeLockerByIdQuery query) {
        return this.exchangeLockerRepository.findById(query.lockerId());
    }

    @Override
    public List<ExchangeLocker> handle(GetAllExchangeLockersByExchangeIdQuery query) {
        Exchange exchange = exchangeRepository.findById(query.exchangeId())
                .orElseThrow(() -> new IllegalArgumentException("Exchange not found with id: " + query.exchangeId()));
        return exchangeLockerRepository.findExchangeLockersByExchangeId((exchange));
    }

    @Override
    public List<ExchangeLocker> handle(GetAllExchangeLockersQuery query) {
        var exchangeLockers = exchangeLockerRepository.findAll();
        return exchangeLockers.isEmpty() ? List.of() : exchangeLockers;
    }
}
