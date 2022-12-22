package com.imp.productservice.presentation;

import com.imp.productservice.application.ProductService;
import com.imp.productservice.application.dto.ProductCreateRequest;
import com.imp.productservice.application.dto.ProductFindResponse;
import com.imp.productservice.application.dto.ProductQuantityUpdateRequest;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody final ProductCreateRequest productCreateRequest,
                                       @SessionAttribute("id") final Long memberId) {
        final long id = productService.create(productCreateRequest, memberId);
        return ResponseEntity.created(URI.create("/products" + id)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductFindResponse> find(@PathVariable final Long id) {
        final ProductFindResponse productFindResponse = productService.findById(id);
        return ResponseEntity.ok(productFindResponse);
    }

    @PatchMapping
    public ResponseEntity<Void> update(@RequestBody final ProductQuantityUpdateRequest request,
                                       @SessionAttribute("id") final Long memberId) {
        productService.updateQuantity(request, memberId);
        return ResponseEntity.noContent().build();
    }
}
