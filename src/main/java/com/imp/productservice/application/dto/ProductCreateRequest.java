package com.imp.productservice.application.dto;

import com.imp.productservice.domain.Price;
import com.imp.productservice.domain.Product;
import com.imp.productservice.domain.Quantity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductCreateRequest {
    @NotBlank
    private String name;
    @Positive
    private BigDecimal price;
    @PositiveOrZero
    private Long quantity;

    public ProductCreateRequest(final String name, final BigDecimal price, final Long quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Product toEntity(final Long sellerId) {
        return new Product(name, new Price(price), new Quantity(quantity), sellerId);
    }
}
