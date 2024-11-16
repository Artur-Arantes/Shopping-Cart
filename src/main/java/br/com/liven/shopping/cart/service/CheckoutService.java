package br.com.liven.shopping.cart.service;

import br.com.liven.shopping.cart.domain.*;
import br.com.liven.shopping.cart.dto.OrderCheckoutOutPutDto;
import br.com.liven.shopping.cart.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CheckoutService {
    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Transactional
    public OrderCheckoutOutPutDto checkout(Cart cart) {
        if (orderRepository.findByCart(cart).isPresent()) {
            throw new DuplicateKeyException("Already exist a order for this cart");
        }


        cart.getProducts().forEach(prd -> {
            Product product = productService.getProductFromId(prd.getId().getProductId());
            productService.hasStock(prd.getQuantity(), product);
            product.getInventory().setQuantity(product.getInventory().getQuantity().subtract(prd.getQuantity()));
            productService.updateProduct(product);
        });

        Set<ProductOrder> products = cart.getProducts().stream()
                .map(ProductCart::toProductOrder)
                .collect(Collectors.toSet());


        final var totalAmount = cart.getProducts().stream()
                .map(ProductCart::getTotalItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final var order = Order.builder()
                .cart(cart)
                .user(cart.getUser())
                .products(products.stream().toList())
                .amount(totalAmount)
                .build();

        order.getProducts().forEach(p -> {
            p.setOrder(order);
            p.setId(ProductOrderId.builder()
                    .productId(p.getProduct().getSku())
                    .orderId(order.getId())
                    .build());
        });
        final var savedOrder = orderRepository.save(order);

        return savedOrder.toCheckoutOutPut();

    }

}
