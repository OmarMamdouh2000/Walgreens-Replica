package com.agmadnasfelguc.walgreensreplica.user.repository;

import com.agmadnasfelguc.walgreensreplica.user.model.Admin;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, String>{

    @Query(value = "SELECT admin_id AS admin_id, status AS status, message AS message FROM login_admin(:username, :password)", nativeQuery = true)
    Tuple loginAdmin(@Param("username") String username,
                     @Param("password") String password);
}
