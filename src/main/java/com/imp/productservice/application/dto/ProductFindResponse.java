package com.imp.productservice.application.dto;

import com.imp.productservice.domain.Product;
import com.imp.productservice.domain.ProductStatus;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductFindResponse {

    private final Long id;
    private final String name;
    private final long quantity;
    private final BigDecimal price;
    private final ProductStatus status;
    private final String sellerName;
    private final String email;

    @Builder
    public ProductFindResponse(final Long id, final String name, final long quantity, final BigDecimal price,
                               final ProductStatus status, final String sellerName, final String email) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.sellerName = sellerName;
        this.email = email;
    }

    public static ProductFindResponse from(final Product product, final Member member) {
        return ProductFindResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .quantity(product.getQuantity().getValue())
                .price(product.getPrice().getValue())
                .status(product.getStatus())
                .sellerName(member.getNickname())
                .email(member.getEmail())
                .build();
    }
}
