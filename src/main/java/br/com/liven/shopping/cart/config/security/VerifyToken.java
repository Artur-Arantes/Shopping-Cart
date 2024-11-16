package br.com.liven.shopping.cart.config.security;

import br.com.liven.shopping.cart.exception.ObjectNotFoundException;
import br.com.liven.shopping.cart.repository.PersonRepository;
import br.com.liven.shopping.cart.repository.UserRepository;
import br.com.liven.shopping.cart.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class VerifyToken extends OncePerRequestFilter {
    @Autowired
    private TokenService service;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final var authHeader = request.getHeader("Authorization");
        if (!Objects.isNull(authHeader)) {
            final var token = authHeader.replace("Bearer", "").trim();
            final var login = service.verifyToken(token);
            UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthenticationToken(login);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String login) {
        final var person = personRepository.findById(login).
                orElseThrow(() -> new ObjectNotFoundException("Person Not Found"));
        final var user = userRepository.findByPerson(person).
                orElseThrow(() -> new ObjectNotFoundException("User Not Found"));
        return new UsernamePasswordAuthenticationToken(user,
                null, user.getAuthorities());
    }
}