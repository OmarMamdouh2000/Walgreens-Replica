package com.agmadnasfelguc.walgreensreplica.user.repository;

import com.agmadnasfelguc.walgreensreplica.user.model.User;
import com.agmadnasfelguc.walgreensreplica.user.model.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM login(:p_email, :p_password)", nativeQuery = true)
    List<String> loginUser(@Param("p_email") String email,
                           @Param("p_password") String password);

    @Query(value = "SELECT * FROM register_user(:p_first_name,:p_last_name,:p_email, :p_password)", nativeQuery = true)
    String registerUser(@Param("p_first_name") String firstName,
                      @Param("p_last_name") String lastName,
                      @Param("p_email") String email,
                      @Param("p_password") String password);

    @Query(value = "SELECT * FROM change_email(:p_user_id, :p_password, :p_new_email)", nativeQuery = true)
    List<String> changeEmail(@Param("p_user_id") String id,
                     @Param("p_password") String password,
                     @Param("p_new_email") String email);


    @Query(value = "SELECT * FROM change_password(:p_user_id, :p_old_password, :p_new_password)", nativeQuery = true)
    List<String> changePassword(@Param("p_user_id") String id,
                        @Param("p_old_password") String oldPassword,
                        @Param("p_new_password") String newPassword);

    @Query(value = "SELECT * FROM edit_personal_details(:p_user_id, :p_address, :p_date_of_birth, cast(:p_gender as \"Gender\"), :p_phone_number, :p_extension)", nativeQuery = true)
    List<String> editUser(@Param("p_user_id") String id,
                  @Param("p_address") String address,
                  @Param("p_date_of_birth") Date dateOfBirth,
                  @Param("p_gender") Gender gender,
                  @Param("p_phone_number") String phoneNumber,
                  @Param("p_extension") String extension);





}
