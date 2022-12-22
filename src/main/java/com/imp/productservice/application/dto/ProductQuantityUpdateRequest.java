package com.imp.productservice.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductQuantityUpdateRequest {

    private Long id;
    private Long quantity;

    public ProductQuantityUpdateRequest(final Long id, final Long quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
