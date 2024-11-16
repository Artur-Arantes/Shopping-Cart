package br.com.liven.shopping.cart.service;

import br.com.liven.shopping.cart.domain.*;
import br.com.liven.shopping.cart.dto.GetCartOutPutDto;
import br.com.liven.shopping.cart.dto.OrderCheckoutOutPutDto;
import br.com.liven.shopping.cart.dto.UpdateCartInPutDto;
import br.com.liven.shopping.cart.exception.NegativeQuantityException;
import br.com.liven.shopping.cart.exception.ObjectNotFoundException;
import br.com.liven.shopping.cart.repository.CartRepository;
import br.com.liven.shopping.cart.repository.ProductRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository repository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final CartStateService cartStateService;

    public CartService(CartRepository repository, UserService userService, ProductRepository productRepository, CartStateService cartStateService) {
        this.repository = repository;
        this.userService = userService;
        this.productRepository = productRepository;
        this.cartStateService = cartStateService;
    }

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

    private void setProductsListForEmptyCart(UpdateCartInPutDto inPut, Cart cart,
                                             List<ProductCart> newListProductCart) {
        inPut.products().forEach(prd -> {
            Product product = getProductFromId(prd.sku());
            if (hasStock(prd.quantity(), product)) {
                ProductCart productCart = fillProductCart(prd.quantity(), product, cart);
                newListProductCart.add(productCart);
            }
        });
    }

    private Product getProductFromId(long id) {
        return productRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Product with SKU " + id + " does not exist"));

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
                .cart(cart)
                .build();
    }

    private boolean hasStock(final BigDecimal quantity, final Product product) {
        BigDecimal inventoryUpdated = product.getInventory().getQuantity().subtract(quantity);

        if (inventoryUpdated.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeQuantityException("Insufficient stock for product SKU: " + product.getSku());
        }
        return true;
    }

    @Transactional
    public OrderCheckoutOutPutDto checkout(long id, final String email) {
        Cart cart = getValidCart(id, email);

        if (cart.getProducts().isEmpty()) {
            throw new IllegalStateException("Cant do the checkout with empty cart");
        }
        //     return CheckoutService.checkout(cart);
        return null;
    }

    public GetCartOutPutDto getCartById(final long id, final String email) {
        return getValidCart(id, email).toGetCartOutPutDto();
    }
}
