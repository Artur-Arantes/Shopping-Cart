package br.com.liven.shopping.cart.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;

@MappedSuperclass
@RequiredArgsConstructor
@Getter
@ToString
public abstract class BaseEntity {

    @Version
    protected Integer version;

    protected ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        createdAt = updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}
