package com.moviegoer.backend.accounts;

import org.springframework.security.core.GrantedAuthority;

public class MoviegoerGrantedAuthority implements GrantedAuthority {

    private final Role role;

    public MoviegoerGrantedAuthority(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.name();
    }

    @Override
    public boolean equals(Object obj) {

        // as-is
        if (this == obj) {
            return true;
        }

        // 형변환
        if (obj instanceof MoviegoerGrantedAuthority) {
            return role.equals(((MoviegoerGrantedAuthority) obj).role);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }

    @Override
    public String toString() {
        return this.role.name();
    }
}
