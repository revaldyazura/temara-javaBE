package com.temara.backend.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.temara.backend.entity.User;
import com.temara.backend.repository.UserRepository;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication)throws AuthenticationException{

        String email = authentication.getPrincipal().toString();

        String pw = authentication.getCredentials().toString();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null && user.getPassword().equals(pw)) {
            return new UsernamePasswordAuthenticationToken(email, pw, new ArrayList<>());
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication){
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
