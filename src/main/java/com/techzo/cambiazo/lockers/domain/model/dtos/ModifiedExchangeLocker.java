package com.techzo.cambiazo.lockers.domain.model.dtos;

import com.techzo.cambiazo.lockers.domain.model.entities.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifiedExchangeLocker {
    private String pinDeposit;
    private String pinRetrieve;
    private String lockerDepositId;
    private String lockerRetrieveId;
    private Location location;
    private String stateExchangeLockerDeposit;
    private String stateExchangeLockerRetrieve;

    public ModifiedExchangeLocker(String pinDeposit, String pinRetrieve, String lockerDepositId, String lockerRetrieveId, Location location, String stateExchangeLockerDeposit, String stateExchangeLockerRetrieve) {
        this.pinDeposit = pinDeposit;
        this.pinRetrieve = pinRetrieve;
        this.lockerDepositId = lockerDepositId;
        this.lockerRetrieveId = lockerRetrieveId;
        this.location = location;
        this.stateExchangeLockerDeposit = stateExchangeLockerDeposit;
        this.stateExchangeLockerRetrieve = stateExchangeLockerRetrieve;
    }

}
