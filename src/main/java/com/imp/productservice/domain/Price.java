package com.imp.productservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Price {
    @Column(name = "price")
    private BigDecimal value;

    public Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal value) {
        if (value.compareTo(BigDecimal.ONE) < 0) {
            throw new IllegalArgumentException("가격은 1보다 작을 수 없다.");
        }
    }
}
