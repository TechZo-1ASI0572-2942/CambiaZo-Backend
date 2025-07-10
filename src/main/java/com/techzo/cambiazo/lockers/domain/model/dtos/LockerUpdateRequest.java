package com.techzo.cambiazo.lockers.domain.model.dtos;

import java.util.Date;

public class LockerUpdateRequest {
    public Long exchange_id;
    public Long user_deposit_id;
    public Long user_retrieve_id;
    public String pin_deposit;
    public String pin_retrieve;
    public String state_exchange;
    public String state;
    public Date last_synced;
}