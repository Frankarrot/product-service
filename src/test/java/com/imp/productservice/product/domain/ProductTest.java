package com.imp.productservice.product.domain;

import static com.imp.productservice.support.ProductFixtures.PHONE;
import static org.assertj.core.api.Assertions.assertThat;

import com.imp.productservice.domain.Product;
import com.imp.productservice.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProductTest {

    @DisplayName("수량이 0인 경우 상품 상태가 soldOut이다.")
    @CsvSource(value = {"0,true", "1,false"})
    @ParameterizedTest
    void isSoldOut_quantityIsZero_returnTrue(final Long quantity, final boolean value) {
        // given
        final Product product = PHONE.create(new Quantity(quantity), 1L);

        // when
        final boolean actual = product.isSoldOut();

        // then
        assertThat(actual).isEqualTo(value);
    }

    @DisplayName("수량이 양수인 경우 상품 상태가 sale이다.")
    @CsvSource(value = {"0,false", "1,true"})
    @ParameterizedTest
    void isSale_quantityIsPositive_returnTrue(final Long quantity, final boolean value) {
        // given
        final Product product = PHONE.create(new Quantity(quantity), 1L);

        // when
        final boolean actual = product.isSale();

        // then
        assertThat(actual).isEqualTo(value);
    }
}
