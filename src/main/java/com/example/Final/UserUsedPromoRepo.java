package com.example.Final;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;
import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

public interface UserUsedPromoRepo  extends CassandraRepository<UserUsedPromo, UUID> {
    @Query("select * from userusedpromo where userid=:userId and promocodeid=:promoCodeId ALLOW FILTERING")
    UserUsedPromo findUserPromo(@Param("userId") UUID userId, @Param("promoCodeId") String promoCodeId);

    @Query("insert into userusedpromo (userid,promocodeid) values (:userId,:promoCodeId)")
    void insertUserPromo(@Param("userId") UUID userId,@Param("promoCodeId") String promoCodeId);

//select * from userusedpromo where userid=72678a7f-3922-4882-8a18-8dd0181213c2 and promocodeid=4814cd60-6712-49fa-a674-8bd7316e7904 ALLOW FILTERING

	@Query("select * from UserUsedPromo")
	public List<UserUsedPromo> getAll();
}
