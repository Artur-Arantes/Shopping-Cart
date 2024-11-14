package br.com.liven.shopping.cart.service;

import br.com.liven.shopping.cart.domain.Person;
import br.com.liven.shopping.cart.domain.User;
import br.com.liven.shopping.cart.dto.UserDeleteInPutDto;
import br.com.liven.shopping.cart.dto.UserInPutDto;
import br.com.liven.shopping.cart.dto.UserOutPutDto;
import br.com.liven.shopping.cart.enums.EnumUserPermission;
import br.com.liven.shopping.cart.exception.ObjectNotFoundException;
import br.com.liven.shopping.cart.repository.PersonRepository;
import br.com.liven.shopping.cart.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository repository;
    PersonRepository personRepository;

    @Transactional
    public void addUser(final UserInPutDto userInPutDto) {
        User user = toUser(userInPutDto);
        if (personRepository.existsById(user.getPerson().getEmail()) || repository.findByPerson(user.getPerson()).isPresent()) {
            throw new DuplicateKeyException("Already exists an account registered with this email");
        }
        User savedUser = repository.save(user);
        repository.savePermissionForUser(user.getId(), (long) EnumUserPermission.USER.getCode());
    }

    public UserOutPutDto getUser(final String id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            User user = repository.findByPerson(person.get()).orElseThrow(() ->
                    new ObjectNotFoundException("User Not found"));

            return new UserOutPutDto(user);
        }
        throw new ObjectNotFoundException("Person Not Found");
    }

    public void deleteUser(final UserDeleteInPutDto userDeleteInputDto) {
        User user = new User();
        BeanUtils.copyProperties(userDeleteInputDto, user);
        if (repository.existsById(user.getId())) {
            repository.deleteById(user.getId());
        } else {
            throw new ObjectNotFoundException("User Not Found");
        }
    }

    private User toUser(final UserInPutDto userInPutDto) {
        User user = userInPutDto.toUser();
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return user;
    }
}