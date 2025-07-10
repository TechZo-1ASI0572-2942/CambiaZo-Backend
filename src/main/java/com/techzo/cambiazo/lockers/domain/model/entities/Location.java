package com.techzo.cambiazo.lockers.domain.model.entities;

import com.techzo.cambiazo.exchanges.domain.model.entities.District;
import com.techzo.cambiazo.lockers.domain.model.commands.CreateLocationCommand;
import com.techzo.cambiazo.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Location extends AuditableAbstractAggregateRoot<Location> {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    private District districtId;

    public Location(CreateLocationCommand command, District district) {
        this.name = command.name();
        this.address = command.address();
        this.districtId = district;
    }

    public Long getDistrictId() {
        return districtId.getId();
    }
}
