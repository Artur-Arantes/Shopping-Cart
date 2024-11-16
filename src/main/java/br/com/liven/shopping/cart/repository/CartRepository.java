package br.com.liven.shopping.cart.repository;

import br.com.liven.shopping.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {


}
