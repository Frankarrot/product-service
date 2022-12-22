package com.imp.productservice.domain;

public enum ProductStatus {
    SALE,
    SOLD_OUT,
    STOP,
    ;

    public static ProductStatus init(final Long quantity) {
        if (quantity == 0) {
            return SOLD_OUT;
        }
        return SALE;
    }
}
