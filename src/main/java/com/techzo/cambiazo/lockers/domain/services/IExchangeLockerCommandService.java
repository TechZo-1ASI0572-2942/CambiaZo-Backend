package com.techzo.cambiazo.lockers.domain.services;

import com.techzo.cambiazo.lockers.domain.model.commands.CreateExchangeLockerCommand;
import com.techzo.cambiazo.lockers.domain.model.commands.UpdateExchangeLockerCommand;
import com.techzo.cambiazo.lockers.domain.model.entities.ExchangeLocker;

import java.util.List;
import java.util.Optional;

public interface IExchangeLockerCommandService {

    Optional<ExchangeLocker> handle(CreateExchangeLockerCommand command);

    Optional<ExchangeLocker> handle(UpdateExchangeLockerCommand command);

    List<ExchangeLocker> autoGenerateExchangeLockers(Long userDepositId, Long userRetrieveId, Long exchangeId);
}
