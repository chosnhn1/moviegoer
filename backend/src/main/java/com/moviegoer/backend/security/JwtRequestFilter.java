package com.moviegoer.backend.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.moviegoer.backend.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;


// Review: Request Filter:: request마다 적용되는 filter
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired private UserDetailsService jwtUserDetailsService;
    @Autowired private JwtUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(
        jakarta.servlet.http.HttpServletRequest request,
        jakarta.servlet.http.HttpServletResponse response,
        jakarta.servlet.FilterChain filterChain
    ) throws jakarta.servlet.ServletException, IOException {
        String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (requestTokenHeader == null) {
            logger.warn("Request does not have token header");
        } else if (!requestTokenHeader.startsWith("Bearer ")) {
            logger.warn("Request has token but not starting with Bearer");
        } else {
            jwt = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwt);
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT", e);
            } catch (ExpiredJwtException e) {
                logger.error("JWT has expired", e);
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                logger.warn("JWT is not valid!");
            }
        }

        filterChain.doFilter(request, response);
    }

}
