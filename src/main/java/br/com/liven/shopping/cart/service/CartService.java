package br.com.liven.shopping.cart.service;

import br.com.liven.shopping.cart.domain.Cart;
import br.com.liven.shopping.cart.domain.Product;
import br.com.liven.shopping.cart.domain.ProductCart;
import br.com.liven.shopping.cart.domain.ProductCartId;
import br.com.liven.shopping.cart.dto.CheckoutInPutDto;
import br.com.liven.shopping.cart.dto.CreateCartOutPutDto;
import br.com.liven.shopping.cart.dto.OrderCheckoutOutPutDto;
import br.com.liven.shopping.cart.dto.UpdateCartInPutDto;
import br.com.liven.shopping.cart.exception.ObjectNotFoundException;
import br.com.liven.shopping.cart.repository.CartRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository repository;
    private final UserService userService;
    private final ProductService productService;
    private final CartStateService cartStateService;
    private final CheckoutService checkoutService;


    public CreateCartOutPutDto getValidCart(final String email) {
        final var user = userService.getUser(email);

        final var newCart = Cart.builder()
                .products(new ArrayList<>())
                .user(user)
                .build();

        return repository.save(newCart).toGetCartOutPutDto();
    }

    @Transactional
    public void update(final UpdateCartInPutDto inPut, final String email) {

        final var cart = getValidCart(inPut.id(), email);
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
        final var cart = repository.findById(id).orElseThrow(() ->
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
        final var cart = getValidCart(input.id(), email);

        if (cart.getProducts().isEmpty()) {
            throw new IllegalStateException("Cant do the checkout with empty cart");
        }
        try {
            return checkoutService.checkout(cart);
        } catch (OptimisticLockException e) {
            throw new ValidationException("Stock was changed by another transaction. Please try again.");
        }
    }

    public CreateCartOutPutDto getCartById(final long id, final String email) {
        return getValidCart(id, email).toGetCartOutPutDto();
    }
}
