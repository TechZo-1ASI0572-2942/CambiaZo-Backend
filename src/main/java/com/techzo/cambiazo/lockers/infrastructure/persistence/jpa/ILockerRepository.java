package com.techzo.cambiazo.lockers.infrastructure.persistence.jpa;

import com.techzo.cambiazo.lockers.domain.model.entities.Location;
import com.techzo.cambiazo.lockers.domain.model.entities.Locker;
import com.techzo.cambiazo.lockers.domain.model.valueObjects.LockerState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ILockerRepository extends JpaRepository<Locker, Long> {
    List<Locker> findLockersByLocationId(Location locationId);

    List<Locker> findLockersByLocationIdAndState(Location locationId, LockerState state);

    Optional<Locker> findLockerByLockerId(String lockerId);
}
