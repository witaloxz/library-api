package com.witalo.libraryapi.security;

import com.witalo.libraryapi.model.User;
import com.witalo.libraryapi.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtCustomAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(byConvert(authentication)){
            String login = authentication.getName();

            if(isUserLogin(login)) {
                User user = userService.getByLogin(login);

                if(user != null){
                    authentication = new CustomAuthentication(user);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean byConvert(Authentication authentication){
        return authentication != null && authentication instanceof JwtAuthenticationToken;
    }

    private boolean isUserLogin(String login) {
        return login != null && login.contains("@");
    }
}