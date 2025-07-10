package com.techzo.cambiazo.lockers.domain.model.entities;

import com.techzo.cambiazo.lockers.domain.model.commands.CreateLockerCommand;
import com.techzo.cambiazo.lockers.domain.model.valueObjects.LockerState;
import com.techzo.cambiazo.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Locker extends AuditableAbstractAggregateRoot<Locker> {


    @Column(nullable = false)
    private String lockerId;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private LockerState state;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location locationId;

    public Locker(CreateLockerCommand command, Location location) {
        this.lockerId = command.lockerId();
        this.state = LockerState.fromString(command.state());
        this.locationId = location;
    }

    public String getLockerState() {
        return state.name();
    }

    public Long getLocationId() {
        return locationId.getId();
    }

    public Locker updateState(LockerState newState) {
        this.state = newState;
        return this;
    }

}
