package com.example.Final;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Table("UserUsedPromo")
public class UserUsedPromo {

    @PrimaryKey
    private UserUsedPromoKey id;

    // Other fields and methods
}

@PrimaryKeyClass
class UserUsedPromoKey implements Serializable {

    @PrimaryKeyColumn(name = "userId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID userId;

    @PrimaryKeyColumn(name = "promoCodeId", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private UUID promoCodeId;

    // Constructors, getters, setters, equals, and hashCode methods
    // Implementations provided below

    public UserUsedPromoKey() {
    }

    public UserUsedPromoKey(UUID userId, UUID promoCodeId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserUsedPromoKey that = (UserUsedPromoKey) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(promoCodeId, that.promoCodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, promoCodeId);
    }
}
