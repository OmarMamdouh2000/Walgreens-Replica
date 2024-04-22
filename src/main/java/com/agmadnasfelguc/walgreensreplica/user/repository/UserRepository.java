package com.agmadnasfelguc.walgreensreplica.user.repository;

import com.agmadnasfelguc.walgreensreplica.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

    @Procedure(name="Login")
    public Object loginUser(@Param("email") String email,
                      @Param("password") String password);

    @Procedure(name = "register_user")
    public Object registerUser(@Param("p_first_name") String firstName,
                      @Param("p_last_name") String lastName,
                      @Param("p_email") String email,
                      @Param("p_password") String password);

    @Procedure(name = "ChangeEmail")
    public Object changeEmail(@Param("p_user_id") String id,
                               @Param("p_email") String email);


    @Procedure(name = "ChangePassword")
    public Object changePassword(@Param("p_user_id") String id,
                                 @Param("p_password") String password);

    @Procedure(name = "edit_personal_details")
    public Object editUser(@Param("p_user_id") String id,
                           @Param("p_address") String address,
                           @Param("p_date_of_birth") Date dateOfBirth,
                           @Param("p_gender") Gender gender,
                           @Param("p_phone_number") String phoneNumber,
                           @Param("p_extension") String extension);





}
