package br.com.liven.shopping.cart.service;

import br.com.liven.shopping.cart.domain.Person;
import br.com.liven.shopping.cart.repository.PersonRepository;
import br.com.liven.shopping.cart.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticatedAuthorizationManager implements UserDetailsService {

    PersonRepository personRepository;
    UserRepository userRepository;


    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {
        Person person = personRepository.findById(username).orElseThrow(() ->
                new UsernameNotFoundException("Person Not Found"));
        return userRepository.findByPerson(person).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }
}
