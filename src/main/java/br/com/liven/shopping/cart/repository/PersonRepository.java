package br.com.liven.shopping.cart.repository;

import br.com.liven.shopping.cart.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, String> {

}
