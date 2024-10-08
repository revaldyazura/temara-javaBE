package com.temara.backend.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.temara.backend.controller.HttpLogger;
import com.temara.backend.exception.GeneralException;
import com.temara.backend.security.service.JwtUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

  @Autowired
  private JwtUserDetailsService jwtUserDetailsService;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  HttpLogger httpLogger;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    if (request.getMethod().equals("GET") || request.getMethod().equals("DEL"))
      httpLogger.trace(request, null);

    if (!request.getServletPath().toString().equals("/security")) {
      final String reqAuthTokenHeader = request.getHeader("Authorization");

      String username = null;
      String jwt = null;

      if (reqAuthTokenHeader != null && reqAuthTokenHeader.startsWith("Bearer ")) {
        jwt = reqAuthTokenHeader.substring(7);
        try {
          username = jwtUtil.getUsernameFromToken(jwt);
        } catch (IllegalArgumentException e) {
          throw new GeneralException("Invalid access", e);
        } catch (ExpiredJwtException e) {
          throw new GeneralException("Session no longer valid, re-login", e);
        }
      } else {
        logger.warn("JWT not exist / not start with bearer string");
      }

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

        if (jwtUtil.validateToken(jwt, userDetails)) {
          UsernamePasswordAuthenticationToken usernamePassAuthenticationToken = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());
          usernamePassAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          usernamePassAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(usernamePassAuthenticationToken);
        }
      }
    }
    chain.doFilter(request, response);
  }
}
