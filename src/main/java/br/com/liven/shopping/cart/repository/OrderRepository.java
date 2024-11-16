package br.com.liven.shopping.cart.repository;

import br.com.liven.shopping.cart.domain.Cart;
import br.com.liven.shopping.cart.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByCart(Cart cart);
}
