package br.com.liven.shopping.cart.enums;

import lombok.Getter;

@Getter
public enum EnumUserPermission {

    USER("ROLE_USER",2),
    ADMIN("ROLE_ADMIN",1);

    private final String role;
    private final int code;

    EnumUserPermission(String role, int code) {
        this.role = role;
        this.code = code;
    }
}