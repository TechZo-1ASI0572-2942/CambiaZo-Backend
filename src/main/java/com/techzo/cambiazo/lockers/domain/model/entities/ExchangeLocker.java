package com.techzo.cambiazo.lockers.domain.model.entities;

import com.techzo.cambiazo.exchanges.domain.model.entities.Exchange;
import com.techzo.cambiazo.lockers.domain.model.commands.CreateExchangeLockerCommand;
import com.techzo.cambiazo.lockers.domain.model.commands.UpdateExchangeLockerCommand;
import com.techzo.cambiazo.lockers.domain.model.valueObjects.LockerExchangeState;
import com.techzo.cambiazo.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExchangeLocker extends AuditableAbstractAggregateRoot<ExchangeLocker> {

    @Column(nullable = false)
    private String pinDeposit;

    @Column(nullable = false)
    private String pinRetrieve;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private LockerExchangeState state;

    @Column(nullable = false)
    private Long userDepositId;

    @Column(nullable = false)
    private Long userRetrieveId;

    @ManyToOne
    @JoinColumn(name = "locker_id", nullable = false)
    private Locker lockerId;

    @ManyToOne
    @JoinColumn(name = "exchange_id", nullable = false)
    private Exchange exchangeId;

    public ExchangeLocker(CreateExchangeLockerCommand command, Locker locker, Exchange exchange) {
        this.pinDeposit = command.pinDeposit();
        this.pinRetrieve = command.pinRetrieve();
        this.state = LockerExchangeState.fromString(command.state());
        this.userDepositId = command.userDepositId();
        this.userRetrieveId = command.userRetrieveId();
        this.lockerId = locker;
        this.exchangeId = exchange;
    }

}
