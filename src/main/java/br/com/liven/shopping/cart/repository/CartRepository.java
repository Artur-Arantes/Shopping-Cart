package br.com.liven.shopping.cart.repository;

import br.com.liven.shopping.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM cart WHERE id_user = :userId AND closed = false")
    Optional<Cart> findCarOpenByUserId(@Param("userId") long userId);



}
