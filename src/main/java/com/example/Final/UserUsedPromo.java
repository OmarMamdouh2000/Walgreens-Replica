package com.example.Final;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;

import java.util.UUID;

@Table("UserUsedPromo")
public class UserUsedPromo {

    @PrimaryKeyColumn( ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID userId;

    @PrimaryKeyColumn( ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)

    private UUID promoCodeId;

    public UserUsedPromo(UUID userId, UUID promoCodeId) {
        this.userId = userId;
        this.promoCodeId = promoCodeId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(UUID promoCodeId) {
        this.promoCodeId = promoCodeId;
    }
}