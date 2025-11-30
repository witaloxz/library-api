package com.witalo.libraryapi.security;

import com.witalo.libraryapi.model.User;
import com.witalo.libraryapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userService.getByLogin(login);

        if (user == null) {
            throw getUserNotFound();
        }

        String encryptedPassword = user.getPassword();

        boolean samePasswords = passwordEncoder.matches(password, encryptedPassword);

        if(samePasswords){
            CustomAuthentication customAuth = new CustomAuthentication(user);
            customAuth.setAuthenticated(true);
            return customAuth;
        }

        throw getUserNotFound();


    }

    private static UsernameNotFoundException getUserNotFound() {
        return new UsernameNotFoundException("User not found");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}