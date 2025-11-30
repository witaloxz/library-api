package com.witalo.libraryapi.security;

import com.witalo.libraryapi.model.User;
import com.witalo.libraryapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    @Autowired
    private UserService userService;

    public User getByLoginUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       if(authentication instanceof CustomAuthentication customAuthentication){
           return customAuthentication.getUser();
       }

       return null;
    }
}