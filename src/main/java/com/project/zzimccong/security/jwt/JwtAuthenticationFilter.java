package com.project.zzimccong.security.jwt;

import com.project.zzimccong.security.service.corp.CustomCorpDetailsService;
import com.project.zzimccong.security.service.user.CustomUserDetailsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomCorpDetailsService corpDetailsService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, CustomCorpDetailsService corpDetailsService, CustomUserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.corpDetailsService = corpDetailsService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;
        String userId = null;
        String userType = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try {
                userId = jwtTokenUtil.getUserIdFromToken(token);
                userType = jwtTokenUtil.getUserTypeFromToken(token);
            } catch (Exception e) {
                logger.error("Error parsing JWT token", e);
            }
        }

        if (userId != null && userType != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;

            // 소문자와 대문자 구분 없이 userType 비교
            if ("corp".equalsIgnoreCase(userType)) {
                userDetails = corpDetailsService.loadUserByUsername(userId);
            } else if ("user".equalsIgnoreCase(userType)) {
                userDetails = userDetailsService.loadUserByUsername(userId);
            }

            if (userDetails != null && jwtTokenUtil.validateToken(token)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }




}
