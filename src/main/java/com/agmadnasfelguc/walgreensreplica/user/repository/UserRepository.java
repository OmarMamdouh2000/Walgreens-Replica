package com.agmadnasfelguc.walgreensreplica.user.repository;

import com.agmadnasfelguc.walgreensreplica.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM login(:email, :password)", nativeQuery = true)
    public List<String> loginUser(@Param("email") String email,
                                  @Param("password") String password);





}
