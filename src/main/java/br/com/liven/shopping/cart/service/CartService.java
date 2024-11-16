package br.com.liven.shopping.cart.service;

import br.com.liven.shopping.cart.domain.*;
import br.com.liven.shopping.cart.dto.CheckoutInPutDto;
import br.com.liven.shopping.cart.dto.GetCartOutPutDto;
import br.com.liven.shopping.cart.dto.OrderCheckoutOutPutDto;
import br.com.liven.shopping.cart.dto.UpdateCartInPutDto;
import br.com.liven.shopping.cart.exception.ObjectNotFoundException;
import br.com.liven.shopping.cart.repository.CartRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository repository;
    private final UserService userService;
    private final ProductService productService;
    private final CartStateService cartStateService;
    private final CheckoutService checkoutService;


    public GetCartOutPutDto getValidCart(final String email) {
        User user = userService.getUser(email);

        Cart newCart = Cart.builder()
                .products(new ArrayList<>())
                .user(user)
                .build();

        return repository.save(newCart).toGetCartOutPutDto();
    }

    @Transactional
    public void update(final UpdateCartInPutDto inPut, final String email) {

        Cart cart = getValidCart(inPut.id(), email);
        List<ProductCart> newListProductCart = new ArrayList<>();

        if (!cart.getProducts().isEmpty()) {
            cart.getProducts().clear();
        }
        setProductsListForEmptyCart(inPut, cart, newListProductCart);
        cart.getProducts().addAll(newListProductCart);
        repository.save(cart);
    }

    private void setProductsListForEmptyCart(final UpdateCartInPutDto inPut, final Cart cart,
                                             List<ProductCart> newListProductCart) {
        inPut.products().forEach(prd -> {
            Product product = productService.getProductFromId(prd.sku());
            productService.hasStock(prd.quantity(), product);
            ProductCart productCart = fillProductCart(prd.quantity(), product, cart);
            newListProductCart.add(productCart);
        });
    }


    private Cart getValidCart(final long id, final String email) {
        Cart cart = repository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Cart with ID " + id + " does not exist"));

        if (!cart.getUser().getPerson().getEmail().equalsIgnoreCase(email)) {
            throw new ValidationException("Only the owner of the cart can make modifications");
        }
        if (cartStateService.isCartExpired(cart.getUpdatedAt())) {
            throw new ValidationException("this cart has expired , please get new cart");
        }

        return cart;
    }

    private static ProductCart fillProductCart(final BigDecimal input, final Product product, final Cart cart) {
        return ProductCart.builder()
                .id(ProductCartId.builder()
                        .cartId(cart.getId())
                        .productId(product.getSku())
                        .build())
                .product(product)
                .quantity(input)
                .totalItem(input.multiply(product.getPrice()))
                .cart(cart)
                .build();
    }

    @Transactional
    public OrderCheckoutOutPutDto processCheckout(final CheckoutInPutDto input, final String email) {
        Cart cart = getValidCart(input.id(), email);

        if (cart.getProducts().isEmpty()) {
            throw new IllegalStateException("Cant do the checkout with empty cart");
        }
        try {
            return checkoutService.checkout(cart);
        } catch (OptimisticLockException e) {
            throw new ValidationException("Stock was changed by another transaction. Please try again.");
        }
    }

    public GetCartOutPutDto getCartById(final long id, final String email) {
        return getValidCart(id, email).toGetCartOutPutDto();
    }

    public void saveCart(Cart cart) {
        repository.save(cart);
    }
}
