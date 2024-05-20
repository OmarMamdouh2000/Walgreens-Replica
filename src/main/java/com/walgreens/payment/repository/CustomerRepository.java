package com.walgreens.payment.repository;

import com.walgreens.payment.model.CustomerDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerDto, UUID> {

//    @Procedure
//    void create_account(UUID user_id);

//    @Procedure(name = "public.create_account")
//    void createAccount(@Param("p_user_id") UUID user_id);

    @Procedure(name = "create_customer")
    void create_customer(@Param("p_customer_uuid") UUID customerUuid, @Param("p_customer_id") String userId);

    @Procedure(name = "get_customer")
    String get_customer(@Param("p_customer_uuid") UUID customerUuid);

    @Procedure(name = "get_loyalty_points")
    int get_loyalty_points(@Param("p_customer_uuid") UUID customerUuid);





    // Query Method or Finder Method
//    Account findByAccountId(UUID accountId);

}
