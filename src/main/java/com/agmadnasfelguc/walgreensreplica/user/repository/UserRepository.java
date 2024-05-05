package com.agmadnasfelguc.walgreensreplica.user.repository;

import com.agmadnasfelguc.walgreensreplica.user.model.User;
import com.agmadnasfelguc.walgreensreplica.user.model.enums.Gender;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.LoginResult;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT user_id AS user_id, status AS status, message AS message, role AS role, first_name AS first_name, last_name AS last_name, email AS email FROM login(:p_email, :p_password)", nativeQuery = true)
    Tuple loginUser(@Param("p_email") String email,
                    @Param("p_password") String password);

    @Query(value = "SELECT * FROM register_user(:p_first_name,:p_last_name,:p_email, :p_password)", nativeQuery = true)
    String registerUser(@Param("p_first_name") String firstName,
                      @Param("p_last_name") String lastName,
                      @Param("p_email") String email,
                      @Param("p_password") String password);

    @Query(value = "SELECT status AS status, message AS message FROM change_email(:p_user_id, :p_password, :p_new_email)", nativeQuery = true)
    Tuple changeEmail(@Param("p_user_id") UUID id,
                     @Param("p_password") String password,
                     @Param("p_new_email") String email);


    @Query(value = "SELECT status AS status, message AS message FROM change_password(:p_user_id, :p_old_password, :p_new_password)", nativeQuery = true)
    Tuple changePassword(@Param("p_user_id") UUID id,
                        @Param("p_old_password") String oldPassword,
                        @Param("p_new_password") String newPassword);

    @Query(value = "SELECT status AS status, message AS message FROM edit_personal_details(:p_user_id, :p_address, :p_date_of_birth, cast(:p_gender as \"Gender\"), :p_phone_number, :p_extension)", nativeQuery = true)
    Tuple editUser(@Param("p_user_id") UUID id,
                  @Param("p_address") String address,
                  @Param("p_date_of_birth") Date dateOfBirth,
                  @Param("p_gender") Gender gender,
                  @Param("p_phone_number") String phoneNumber,
                  @Param("p_extension") String extension);





}
