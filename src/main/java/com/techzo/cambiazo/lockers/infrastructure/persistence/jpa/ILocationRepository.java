package com.techzo.cambiazo.lockers.infrastructure.persistence.jpa;

import com.techzo.cambiazo.lockers.domain.model.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILocationRepository extends JpaRepository<Location, Long> {


}
