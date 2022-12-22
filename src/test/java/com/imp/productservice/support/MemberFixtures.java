package com.imp.productservice.support;

public enum MemberFixtures {

    KUN("ghd700@email.com", Role.ADMIN, "password", "kun"),
    CHAN("gmelddl514@email.com", Role.ADMIN, "password", "chan"),
    ;

    private final String email;
    private final Role role;

    private final String password;

    private final String nickname;

    MemberFixtures(final String email, final Role role, final String password, final String nickname) {
        this.email = email;
        this.role = role;
        this.password = password;
        this.nickname = nickname;
    }

    public Member create() {
        return new Member(role, email, nickname, password);
    }

    public Member createWithRole(final Role role) {
        return new Member(role, email, nickname, password);
    }
}
