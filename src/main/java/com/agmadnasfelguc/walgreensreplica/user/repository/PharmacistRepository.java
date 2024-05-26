package com.agmadnasfelguc.walgreensreplica.user.repository;

import com.agmadnasfelguc.walgreensreplica.user.model.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PharmacistRepository extends JpaRepository<Pharmacist, UUID> {

}
