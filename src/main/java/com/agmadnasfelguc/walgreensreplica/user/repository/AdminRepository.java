package com.agmadnasfelguc.walgreensreplica.user.repository;

import com.agmadnasfelguc.walgreensreplica.user.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface AdminRepository extends JpaRepository<Admin, String>{

    @Procedure(name="Login_Admin")
    public Object loginAdmin(@Param("username") String email,
                            @Param("password") String password);
}
