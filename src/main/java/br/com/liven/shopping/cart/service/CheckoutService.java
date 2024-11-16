package br.com.liven.shopping.cart.service;

import br.com.liven.shopping.cart.domain.Cart;
import br.com.liven.shopping.cart.domain.Order;
import br.com.liven.shopping.cart.domain.Product;
import br.com.liven.shopping.cart.domain.ProductCart;
import br.com.liven.shopping.cart.repository.CartRepository;
import br.com.liven.shopping.cart.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CheckoutService {
    @Autowired
    private static CartRepository cartRepository;
    @Autowired
    private static OrderRepository orderRepository;

    @Transactional
    public static synchronized Order checkout(Cart cart) {
        Set<Product> products = cart.getProducts().stream()
                .map(ProductCart::getProduct)
                .collect(Collectors.toSet());

        BigDecimal totalAmount = cart.getProducts().stream()
                .map(productCart -> productCart.getProduct().getPrice().multiply(productCart.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .user(cart.getUser())
                .products(products.stream().toList())
                .amount(totalAmount)
                .build();

       return orderRepository.save(order);

    }

}
