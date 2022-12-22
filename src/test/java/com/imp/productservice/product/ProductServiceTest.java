//package com.imp.productservice.product;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertAll;
//
//import com.imp.productservice.application.ProductService;
//import com.imp.productservice.application.dto.ProductCreateRequest;
//import com.imp.productservice.application.dto.ProductFindResponse;
//import com.imp.productservice.application.dto.ProductQuantityUpdateRequest;
//import com.imp.productservice.domain.Product;
//import com.imp.productservice.domain.ProductRepository;
//import com.imp.productservice.domain.Quantity;
//import com.imp.productservice.support.ApplicationTest;
//import java.math.BigDecimal;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.EnumSource;
//import org.junit.jupiter.params.provider.EnumSource.Mode;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.beans.factory.annotation.Autowired;
//
//@ApplicationTest
//public class ProductServiceTest {
//    @Autowired
//    private ProductService productService;
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private ProductRepository productRepository;
//
//    @DisplayName("상품을 등록할 수 있다")
//    @Test
//    void create_new_product() {
//        // given
//        final Member member = KUN.create();
//        final Member savedMember = memberRepository.save(member);
//        final ProductCreateRequest request = new ProductCreateRequest("상품명", BigDecimal.valueOf(30000L), 10L);
//
//        // when
//        final long actual = productService.create(request, savedMember.getId());
//
//        // then
//        assertThat(actual).isNotZero();
//    }
//
//    @DisplayName("존재하지 않는 회원의 경우 상품 등록에 실패한다")
//    @Test
//    void create_product_should_throw_if_sellerId_is_invalid() {
//        // given
//        final ProductCreateRequest request = new ProductCreateRequest("존재하지 않는 상품", BigDecimal.valueOf(300L), 10L);
//
//        // when & then
//        assertThatThrownBy(() -> productService.create(request, 999L))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("존재하지 않는 판매자 아이디");
//    }
//
//    @DisplayName("회원의 ROLE이 판매자가 아닌 경우 상품 등록에 실패한다.")
//    @Test
//    void create_MemberIsNotSeller_throwsException() {
//        // given
//        final Member member = KUN.createWithRole(Role.BUYER);
//        final Member savedMember = memberRepository.save(member);
//        final ProductCreateRequest request = new ProductCreateRequest("존재하지 않는 상품", BigDecimal.valueOf(300L), 10L);
//
//        // when, then
//        assertThatThrownBy(() -> productService.create(request, savedMember.getId()))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("역할이 판매자여야만 상품을 등록할 수 있습니다.");
//    }
//
//    @DisplayName("상품 가격이 1 보다 작을 경우 상품 등록에 실패한다")
//    @ValueSource(ints = {-10, -1, 0})
//    @ParameterizedTest
//    void create_product_should_throw_if_price_is_zero_or_negative(final int price) {
//        // given
//        final ProductCreateRequest request = new ProductCreateRequest("상품명", BigDecimal.valueOf(price), 10L);
//        final Member savedMember = memberRepository.save(KUN.create());
//
//        // when & then
//        assertThatThrownBy(() -> productService.create(request, savedMember.getId()))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("가격은 1보다 작을 수 없다");
//    }
//
//    @DisplayName("상품 수량이 0 보다 작을 경우 상품 등록에 실패한다")
//    @ValueSource(longs = {-10, -1})
//    @ParameterizedTest
//    void create_product_should_throw_if_quantity_is_zero_or_negative(final long quantity) {
//        // given
//        final ProductCreateRequest request = new ProductCreateRequest("상품명", BigDecimal.valueOf(100L), quantity);
//        final Member savedMember = memberRepository.save(KUN.create());
//
//        // when & then
//        assertThatThrownBy(() -> productService.create(request, savedMember.getId()))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("재고 수량은 0보다 작을 수 없다.");
//    }
//
//    @DisplayName("상품을 생성하고 재고가 1개 이상이면 초기 상태는 SALE이다.")
//    @ValueSource(longs = {1, 2})
//    @ParameterizedTest
//    void product_initial_status_is_sale(final long quantity) {
//        // given
//        final ProductCreateRequest request = new ProductCreateRequest("상품명", BigDecimal.valueOf(100L), quantity);
//        final Member savedMember = memberRepository.save(KUN.create());
//
//        // when
//        final long id = productService.create(request, savedMember.getId());
//        final Product product = productRepository.findById(id).orElseThrow(NullPointerException::new);
//
//        // then
//        assertThat(product.isSale()).isTrue();
//    }
//
//    @DisplayName("상품을 생성하고 재고가 0개면 초기 상태는 SOLD_OUT이다.")
//    @Test
//    void product_initial_status_with_zero_quantity_is_sold_out() {
//        // given
//        final ProductCreateRequest request = new ProductCreateRequest("상품명", BigDecimal.valueOf(100L), 0L);
//        final Member savedMember = memberRepository.save(KUN.create());
//
//        // when
//        final long id = productService.create(request, savedMember.getId());
//        final Product product = productRepository.findById(id).orElseThrow(NullPointerException::new);
//
//        // then
//        assertThat(product.isSoldOut()).isTrue();
//    }
//
//    @DisplayName("상품을 조회한다.")
//    @Test
//    void findById() {
//        // given
//        final Member member = memberRepository.save(KUN.create());
//        final Product product = PHONE.create(new Quantity(3L), member.getId());
//        final Product savedProduct = productRepository.save(product);
//
//        final ProductFindResponse expected = ProductFindResponse.from(savedProduct, member);
//
//        // when
//        final ProductFindResponse productFindResponse = productService.findById(savedProduct.getId());
//
//        // then
//        assertAll(
//                () -> assertThat(expected).usingRecursiveComparison().ignoringFields("price")
//                        .isEqualTo(productFindResponse),
//                () -> assertThat(expected.getPrice()).isEqualByComparingTo(productFindResponse.getPrice())
//        );
//    }
//
//    @DisplayName("판매자가 존재하지 않는 경우 예외가 발생한다.")
//    @Test
//    void updateQuantity_if_seller_not_exist_throwsException() {
//        // given
//        final Member member = memberRepository.save(KUN.create());
//        final Product product = PHONE.create(new Quantity(3L), member.getId());
//        productRepository.save(product);
//
//        final ProductQuantityUpdateRequest request = new ProductQuantityUpdateRequest(
//                product.getId(), 10L);
//
//        // when, then
//        assertThatThrownBy(() -> productService.updateQuantity(request, 999L))
//                .isInstanceOf(IllegalArgumentException.class);
//
//    }
//
//    @DisplayName("존재하지 않는 상품인 경우 예외가 발생한다.")
//    @Test
//    void updateQuantity_if_product_not_exist_throwsException() {
//        // given
//        final Member member = memberRepository.save(KUN.create());
//        final Product product = PHONE.create(new Quantity(3L), member.getId());
//        productRepository.save(product);
//
//        final ProductQuantityUpdateRequest request = new ProductQuantityUpdateRequest(
//                999L, 1L);
//
//        // when, then
//        assertThatThrownBy(() -> productService.updateQuantity(request, member.getId()))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("수량이 음수이면 예외가 발생한다.")
//    @Test
//    void updateQuantity_if_quantity_negative_throwsException() {
//        // given
//        final Member member = memberRepository.save(KUN.create());
//        final Product product = PHONE.create(new Quantity(3L), member.getId());
//        productRepository.save(product);
//
//        final ProductQuantityUpdateRequest request = new ProductQuantityUpdateRequest(
//                product.getId(), -1L);
//
//        // when, then
//        assertThatThrownBy(() -> productService.updateQuantity(request, member.getId()))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("자신이 판매하는 상품이 아닐 경우 예외가 발생한다.")
//    @Test
//    void updateQuantity_if_not_owner_throwsException() {
//        // given
//        final Member member = memberRepository.save(KUN.createWithRole(Role.SELLER));
//        final Member anotherMember = memberRepository.save(CHAN.create());
//        final Product product = PHONE.create(new Quantity(3L), member.getId());
//        productRepository.save(product);
//
//        final ProductQuantityUpdateRequest request = new ProductQuantityUpdateRequest(
//                product.getId(), 1L);
//
//        // when, then
//        assertThatThrownBy(() -> productService.updateQuantity(request, anotherMember.getId()))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("자신이 판매하는 상품일 경우 수량 변경에 성공한다.")
//    @ParameterizedTest
//    @EnumSource(value = Role.class, names = {"SELLER", "ADMIN"}, mode = Mode.INCLUDE)
//    void updateQuantity_if_not_seller_throwsException(final Role role) {
//        // given
//        final Member member = memberRepository.save(KUN.createWithRole(role));
//        final Product product = PHONE.create(new Quantity(3L), member.getId());
//        final Product savedProduct = productRepository.save(product);
//
//        final ProductQuantityUpdateRequest request = new ProductQuantityUpdateRequest(
//                product.getId(), 1L);
//
//        // when
//        productService.updateQuantity(request, member.getId());
//
//        // then
//        assertThat(productRepository.findById(savedProduct.getId()).get().getQuantity().getValue())
//                .isEqualTo(1L);
//    }
//}
