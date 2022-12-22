package com.imp.productservice.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {
    private Boolean isExist;
    private Boolean isSellerOrAdmin;

    public MemberResponse(final Boolean isExist, final Boolean isSellerOrAdmin) {
        this.isExist = isExist;
        this.isSellerOrAdmin = isSellerOrAdmin;
    }
}
