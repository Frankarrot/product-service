package com.imp.productservice.application;

import com.imp.productservice.application.dto.ProductCreateRequest;
import com.imp.productservice.application.dto.ProductFindResponse;
import com.imp.productservice.application.dto.ProductQuantityUpdateRequest;
import com.imp.productservice.client.AuthServiceClient;
import com.imp.productservice.client.MemberResponse;
import com.imp.productservice.domain.Product;
import com.imp.productservice.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final AuthServiceClient authServiceClient;

    public ProductService(final ProductRepository productRepository, final AuthServiceClient authServiceClient) {
        this.productRepository = productRepository;
        this.authServiceClient = authServiceClient;
    }

    public long create(final ProductCreateRequest request, final Long sellerId) {
        final MemberResponse response = authServiceClient.findMember(sellerId);
        if (!response.getIsExist()) {
            throw new IllegalArgumentException("존재하지 않는 판매자 아이디");
        }

        if (!response.getIsSellerOrAdmin()) {
            throw new IllegalArgumentException("역할이 판매자여야만 상품을 등록할 수 있습니다.");
        }

        final Product product = request.toEntity(sellerId);
        final Product savedProduct = productRepository.save(product);

        return savedProduct.getId();
    }

    public ProductFindResponse findById(final Long id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        final MemberResponse response = authServiceClient.findMember(product.getSellerId());
        if (!response.getIsExist()) {
            throw new IllegalArgumentException("존재하지 않는 판매자 아이디");
        }

        return ProductFindResponse.from(product, response);
    }

    @Transactional
    public void updateQuantity(final ProductQuantityUpdateRequest request, final Long sellerId) {
        final Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        final MemberResponse response = authServiceClient.findMember(product.getSellerId());
        if (!response.getIsExist()) {
            throw new IllegalArgumentException("존재하지 않는 판매자 아이디");
        }

        if (!product.isOwnedBy(sellerId) || !response.getIsSellerOrAdmin()) {
            throw new IllegalArgumentException("상품 변경 권한이 없습니다.");
        }
        product.updateQuantity(request.getQuantity());
    }
}
