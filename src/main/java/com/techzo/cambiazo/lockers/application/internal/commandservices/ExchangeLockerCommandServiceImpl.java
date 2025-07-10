package com.techzo.cambiazo.lockers.application.internal.commandservices;

import com.techzo.cambiazo.exchanges.domain.model.entities.Exchange;
import com.techzo.cambiazo.exchanges.infrastructure.persistence.jpa.IExchangeRepository;
import com.techzo.cambiazo.iam.domain.model.aggregates.User;
import com.techzo.cambiazo.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.techzo.cambiazo.lockers.domain.model.commands.CreateExchangeLockerCommand;
import com.techzo.cambiazo.lockers.domain.model.commands.UpdateExchangeLockerCommand;
import com.techzo.cambiazo.lockers.domain.model.dtos.LockerUpdateRequest;
import com.techzo.cambiazo.lockers.domain.model.entities.ExchangeLocker;
import com.techzo.cambiazo.lockers.domain.model.entities.Location;
import com.techzo.cambiazo.lockers.domain.model.entities.Locker;
import com.techzo.cambiazo.lockers.domain.model.valueObjects.LockerExchangeState;
import com.techzo.cambiazo.lockers.domain.model.valueObjects.LockerState;
import com.techzo.cambiazo.lockers.domain.services.IExchangeLockerCommandService;
import com.techzo.cambiazo.lockers.infrastructure.persistence.jpa.IExchangeLockerRepository;
import com.techzo.cambiazo.lockers.infrastructure.persistence.jpa.ILocationRepository;
import com.techzo.cambiazo.lockers.infrastructure.persistence.jpa.ILockerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ExchangeLockerCommandServiceImpl implements IExchangeLockerCommandService {

    private final RestTemplate restTemplate;
    private final IExchangeLockerRepository exchangeLockerRepository;
    private final IExchangeRepository exchangeRepository;
    private final ILockerRepository lockerRepository;
    private final UserRepository userRepository;
    private final ILocationRepository locationRepository;

    public ExchangeLockerCommandServiceImpl(RestTemplate restTemplate, IExchangeLockerRepository exchangeLockerRepository, IExchangeRepository exchangeRepository, ILockerRepository lockerRepository, UserRepository userRepository, ILocationRepository locationRepository) {
        this.restTemplate = restTemplate;
        this.exchangeLockerRepository = exchangeLockerRepository;
        this.exchangeRepository = exchangeRepository;
        this.lockerRepository = lockerRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public Optional<ExchangeLocker> handle(CreateExchangeLockerCommand command) {
        Exchange exchange = exchangeRepository.findById(command.exchangeId())
                .orElseThrow(() -> new IllegalArgumentException("Exchange not found with id: " + command.exchangeId()));
        Locker locker = lockerRepository.findById(command.lockerId())
                .orElseThrow(() -> new IllegalArgumentException("Locker not found with id: " + command.lockerId()));

        var exchangeLocker = new ExchangeLocker(command, locker, exchange);
        exchangeLockerRepository.save(exchangeLocker);
        return Optional.of(exchangeLocker);
    }

    @Override
    public Optional<ExchangeLocker> handle(UpdateExchangeLockerCommand command) {
        Locker locker = lockerRepository.findLockerByLockerId(command.lockerId())
                .orElseThrow(() -> new IllegalArgumentException("Locker not found with id: " + command.lockerId()));

        Exchange exchange = exchangeRepository.findById(command.exchangeId())
                .orElseThrow(() -> new IllegalArgumentException("Exchange not found with id: " + command.exchangeId()));


        ExchangeLocker exchangeLocker = exchangeLockerRepository.findByExchangeIdAndLockerId(exchange, locker)
                .orElseThrow(() -> new IllegalArgumentException("ExchangeLocker not found for exchangeId: " + command.exchangeId() + " and lockerId: " + command.lockerId()));

        switch(command.stateExchange()){
            case "FULL":
                exchangeLocker.setState(LockerExchangeState.FULL);
                exchangeLockerRepository.save(exchangeLocker);
                break;
            case "DELIVERED":
                exchangeLocker.setState(LockerExchangeState.DELIVERED);
                locker.setState(LockerState.AVAILABLE);
                exchangeLockerRepository.save(exchangeLocker);
                lockerRepository.save(locker);
                break;
        }

        List<ExchangeLocker> exchangesLockers = exchangeLockerRepository.findExchangeLockersByExchangeIdAndState(exchange, LockerExchangeState.DELIVERED);
        if(exchangesLockers.size() == 2){
            //change the state of the exchange to "FULL"
            exchange.setStatus("Completado");
            exchangeRepository.save(exchange);
        }


        return Optional.of(exchangeLocker);

    }


    @Override
    public List<ExchangeLocker> autoGenerateExchangeLockers(Long userDepositId, Long userRetrieveId, Long exchangeId) {
        User userDeposit = userRepository.findById(userDepositId)
                .orElseThrow(() -> new IllegalArgumentException("User Deposit not found with id: " + userDepositId));
        User userRetrieve = userRepository.findById(userRetrieveId)
                .orElseThrow(() -> new IllegalArgumentException("User Retrieve not found with id: " + userRetrieveId));
        Exchange exchange = exchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new IllegalArgumentException("Exchange not found with id: " + exchangeId));

        if(exchange!=null){
            Location location = locationRepository.findById(exchange.getLocationId())
                    .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + exchange.getLocationId()));

            List<Locker> lockers = lockerRepository.findLockersByLocationIdAndState(location, LockerState.AVAILABLE);
            if(lockers.size()>=2){
                Locker lockerForFirstUser = lockers.get(0);
                Locker lockerForSecondUser = lockers.get(1);

                //generate 4 pins for the lockers with 4 different digits
                String pinDepositForFirstLocker= String.valueOf((int) (Math.random() * 9000) + 1000); // 4-digit pin
                String pinRetrieveForFirstLocker = String.valueOf((int) (Math.random() * 9000) + 1000); // 4-digit pin

                String pinDepositForSecondLocker = String.valueOf((int) (Math.random() * 9000) + 1000); // 4-digit pin
                String pinRetrieveForSecondLocker = String.valueOf((int) (Math.random() * 9000) + 1000); // 4-digit pin

                var exchangeLockerCommandForFirstUser = new CreateExchangeLockerCommand(
                        pinDepositForFirstLocker, pinRetrieveForFirstLocker, "EMPTY",
                        userDepositId,userRetrieveId, lockerForFirstUser.getId(), exchangeId);


                var exchangeLockerCommandForSecondUser = new CreateExchangeLockerCommand(
                        pinDepositForSecondLocker, pinRetrieveForSecondLocker, "EMPTY",
                        userRetrieveId, userDepositId, lockerForSecondUser.getId(), exchangeId);


                ExchangeLocker exchangeLockerForFirstUser = new ExchangeLocker(exchangeLockerCommandForFirstUser, lockerForFirstUser, exchange);
                ExchangeLocker exchangeLockerForSecondUser = new ExchangeLocker(exchangeLockerCommandForSecondUser, lockerForSecondUser, exchange);

                exchangeLockerRepository.save(exchangeLockerForFirstUser);
                exchangeLockerRepository.save(exchangeLockerForSecondUser);
                //change the state of the lockers to "IN_USE"
                lockerForFirstUser.setState(LockerState.IN_USE);
                lockerForSecondUser.setState(LockerState.IN_USE);
                lockerRepository.save(lockerForFirstUser);
                lockerRepository.save(lockerForSecondUser);


                // do a put request to the iot edge to update the lockers state
                // Example URL:
                // https://cambiazo-edge-e7fmd6d5dcchhnbc.eastus-01.azurewebsites.net/api/v1/lockers/records/<locker_id>
//                exchange_id=updated_record.exchange_id,
//                user_deposit_id=updated_record.user_deposit_id,
//                user_retrieve_id=updated_record.user_retrieve_id,
//                pin_deposit=updated_record.pin_deposit,
//                pin_retrieve=updated_record.pin_retrieve,
//                state_exchange=updated_record.state_exchange,
//                state=updated_record.state,
//                last_synced=updated_record.last_synced
                //UPDATE FOR FIRST USER LOCKER
                LockerUpdateRequest requestForFirstUserLocker = new LockerUpdateRequest();
                requestForFirstUserLocker.exchange_id = exchangeId;
                requestForFirstUserLocker.user_deposit_id = exchangeLockerForFirstUser.getUserDepositId();
                requestForFirstUserLocker.user_retrieve_id = exchangeLockerForFirstUser.getUserRetrieveId();
                requestForFirstUserLocker.pin_deposit = exchangeLockerForFirstUser.getPinDeposit();
                requestForFirstUserLocker.pin_retrieve = exchangeLockerForFirstUser.getPinRetrieve();
                requestForFirstUserLocker.state_exchange = exchangeLockerForFirstUser.getState().toString();
                requestForFirstUserLocker.state = lockerForFirstUser.getState().toString();
                requestForFirstUserLocker.last_synced = new Date();

                String urlForFirsUserLocker = "https://cambiazo-edge-e7fmd6d5dcchhnbc.eastus-01.azurewebsites.net/api/v1/lockers/records/" + lockerForFirstUser.getLockerId();

                HttpEntity<LockerUpdateRequest> entity = new HttpEntity<>(requestForFirstUserLocker);
                ResponseEntity<String> response = restTemplate.exchange(urlForFirsUserLocker, HttpMethod.PUT, entity, String.class);

                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new RuntimeException("Failed to update locker state for first user: " + response.getStatusCode());
                }

                //UPDATE FOR SECOND USER LOCKER
                LockerUpdateRequest requestForSecondUserLocker = new LockerUpdateRequest();
                requestForSecondUserLocker.exchange_id = exchangeId;
                requestForSecondUserLocker.user_deposit_id = exchangeLockerForSecondUser.getUserDepositId();
                requestForSecondUserLocker.user_retrieve_id = exchangeLockerForSecondUser.getUserRetrieveId();
                requestForSecondUserLocker.pin_deposit = exchangeLockerForSecondUser.getPinDeposit();
                requestForSecondUserLocker.pin_retrieve = exchangeLockerForSecondUser.getPinRetrieve();
                requestForSecondUserLocker.state_exchange = exchangeLockerForSecondUser.getState().toString();
                requestForSecondUserLocker.state = lockerForSecondUser.getState().toString();
                requestForSecondUserLocker.last_synced = new Date();

                String urlForSecondUserLocker = "https://cambiazo-edge-e7fmd6d5dcchhnbc.eastus-01.azurewebsites.net/api/v1/lockers/records/" + lockerForSecondUser.getLockerId();
                HttpEntity<LockerUpdateRequest> entityForSecondUser = new HttpEntity<>(requestForSecondUserLocker);
                ResponseEntity<String> responseForSecondUser = restTemplate.exchange(urlForSecondUserLocker, HttpMethod.PUT, entityForSecondUser, String.class);

                if (!responseForSecondUser.getStatusCode().is2xxSuccessful()) {
                    throw new RuntimeException("Failed to update locker state for second user: " + responseForSecondUser.getStatusCode());
                }

                return List.of(exchangeLockerForFirstUser, exchangeLockerForSecondUser);
            } else {
                throw new IllegalArgumentException("Not enough available lockers for the exchange.");
            }
        }

        return List.of(); // Return an empty list if no lockers are available or exchange is null
    }


}
