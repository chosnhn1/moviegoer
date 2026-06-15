package com.moviegoer.backend.accounts;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// UserDetails Customizing: 로그인 등의 보안 context에서 사용됩니다
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoviegoerUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
}
