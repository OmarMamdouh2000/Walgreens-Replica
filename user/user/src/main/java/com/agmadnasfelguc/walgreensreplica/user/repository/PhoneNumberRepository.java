package com.agmadnasfelguc.walgreensreplica.user.repository;

import com.agmadnasfelguc.walgreensreplica.user.model.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, UUID> {
}
