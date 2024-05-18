package com.agmadnasfelguc.walgreensreplica.user.repository;

import com.agmadnasfelguc.walgreensreplica.user.model.Admin;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID>{

    @Query(value = "SELECT admin_id AS admin_id, status AS status, message AS message FROM login_admin(:username, :password)", nativeQuery = true)
    Tuple loginAdmin(@Param("username") String username,
                     @Param("password") String password);

    @Query(value = "SELECT status AS status, message AS message FROM ban_account(:p_user_id)", nativeQuery = true)
    Tuple banAccount(@Param("p_user_id") UUID id);

    @Query(value = "SELECT status AS status, message AS message FROM unban_account(:p_user_id)", nativeQuery = true)
    Tuple unbanAccount(@Param("p_user_id") UUID id);

    @Query(value = "SELECT * FROM add_admin(:v_username,:v_password)", nativeQuery = true)
    String addAdmin(@Param("v_username") String username,
                   @Param("v_password") String password);

    @Query(value = "SELECT * FROM add_pharmacist(:p_first_name,:p_last_name,:p_email,:p_password)", nativeQuery = true)
    String addPharmacist(@Param("p_first_name") String firstName,
                   @Param("p_last_name") String lastName,
                   @Param("p_email") String email,
                   @Param("p_password") String password);

    @Query(value = "SELECT * FROM get_all_users()", nativeQuery = true)
    List<Tuple> getAllUsers();
}
