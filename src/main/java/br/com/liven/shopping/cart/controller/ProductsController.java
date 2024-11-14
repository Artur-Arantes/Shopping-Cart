package br.com.liven.shopping.cart.controller;


import br.com.liven.shopping.cart.dto.ProductInPutDto;
import br.com.liven.shopping.cart.dto.ProductOutPutDto;
import br.com.liven.shopping.cart.dto.ProductSearchCriteria;
import br.com.liven.shopping.cart.service.ProductService;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/products")
public class ProductsController {

    private final ProductService service;

    public ProductsController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addProduct(@RequestBody @Valid @NonNull final ProductInPutDto productInPutDto) {
        service.addProduct(productInPutDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<HttpStatus> updateProduct(@RequestBody @Valid @NonNull final ProductInPutDto productInPutDto) {
        service.update(productInPutDto);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<Page<ProductOutPutDto>> listProducts(
            @ModelAttribute final ProductSearchCriteria searchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductOutPutDto> products = service.getFilteredProducts(searchCriteria, pageable);
        return ResponseEntity.ok(products);
    }

}
