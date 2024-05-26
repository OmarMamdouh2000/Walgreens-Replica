package com.example.Final;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PromoRepo extends CassandraRepository<PromoCodeTable, UUID> {
    @Query("select * from promocodes where id=:promoId ALLOW FILTERING")
    PromoCodeTable getPromoCode(@Param("promoId") UUID promoId);

    @Query("select * from promocodes where code=:code ALLOW FILTERING")
    PromoCodeTable getPromoCodeByCode(@Param("code") String code);
}
