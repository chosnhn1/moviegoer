package com.moviegoer.backend.accounts;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MoviegoerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public MoviegoerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾지 못했습니다: " + username));
        
        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(MoviegoerGrantedAuthority::new)
            .collect(Collectors.toList());

        return new MoviegoerUserDetails(user.getId(), username, user.getPassword(), authorities);
    }
}
