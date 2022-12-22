package com.imp.productservice.domain;

import com.imp.productservice.common.BaseTimeEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@Entity
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;
    @Embedded
    private Quantity quantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private Long sellerId;

    public Product(final String name, final Price price, final Quantity quantity, final Long sellerId) {
        this(null, name, price, quantity, sellerId);
    }

    public Product(final Long id, final String name, final Price price, final Quantity quantity, final Long sellerId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.sellerId = sellerId;
        this.status = ProductStatus.init(quantity.getValue());
    }

    public void updateQuantity(final long quantity) {
        this.quantity = this.quantity.update(quantity);
    }

    public boolean isOwnedBy(final Long sellerId) {
        return Objects.equals(this.sellerId, sellerId);
    }

    public boolean isSale() {
        return status == ProductStatus.SALE;
    }

    public boolean isSoldOut() {
        return status == ProductStatus.SOLD_OUT;
    }
}
