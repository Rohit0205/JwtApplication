package com.roh.jwtApplication.enums;

import java.util.Set;

public enum Role {

    USER(Set.of(
            Permission.USER_READ,
            Permission.USER_UPDATE
    )),
    ADMIN(Set.of(
            Permission.USER_READ,
            Permission.USER_CREATE,
            Permission.USER_UPDATE,
            Permission.USER_DELETE

    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
