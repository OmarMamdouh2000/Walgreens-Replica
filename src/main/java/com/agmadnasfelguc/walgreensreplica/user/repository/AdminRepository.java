package com.agmadnasfelguc.walgreensreplica.user.repository;

import com.agmadnasfelguc.walgreensreplica.user.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, String>{

    @Query(value = "SELECT * FROM login_admin(:username, :password)", nativeQuery = true)
    public List<String> loginAdmin(@Param("username") String username,
                                  @Param("password") String password);
}
