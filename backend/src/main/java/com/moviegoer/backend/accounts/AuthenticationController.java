package com.moviegoer.backend.accounts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviegoer.backend.util.JwtUtil;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtTokenUtil;
    private UserDetailsService userDetailsService;
    private UserService userService;

    @Autowired
    public AuthenticationController(
        AuthenticationManager authenticationManager,
        JwtUtil jwtTokenUtil,
        UserDetailsService userDetailsService,
        UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    // 사용자 인증 (토큰 발급, 로그인)
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(
        @RequestBody AuthenticationRequestDto authRequest
    ) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.username(),
                    authRequest.password()
                )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.username());

            UserResponseDto userResponseDto = userService.getUserByUsername(userDetails.getUsername());

            return ResponseEntity.ok(
                AuthenticationResponseDto.builder()
                    .jwt(jwtTokenUtil.generateToken(userDetails, userResponseDto.id()))
                    .build()
            );
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred during authentication");
        }
    }
}
