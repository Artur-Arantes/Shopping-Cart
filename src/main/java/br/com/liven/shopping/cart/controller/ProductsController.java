package br.com.liven.shopping.cart.controller;


import br.com.liven.shopping.cart.dto.*;
import br.com.liven.shopping.cart.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/products")
@AllArgsConstructor
@Tag(name = "Product", description = "Endpoints to GET,Post,Patch product")
public class ProductsController {

    private final ProductService service;


    @PostMapping
    @Operation(summary = "Add Product to database")
    @ApiResponse(responseCode = "200", description = "Product created", content = @Content)
    @ApiResponse(responseCode = "409",
            description = "Conflicted, a product with the given SKU already exists.", content = @Content)
    public ResponseEntity<HttpStatus> addProduct(@RequestBody @Valid @NonNull final ProductInPutDto productInPutDto) {
        service.addProduct(productInPutDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    @Operation(summary = "Edit Product")
    @ApiResponse(responseCode = "200", description = "Checkout Success",
            content = @Content(schema = @Schema(implementation = OrderCheckoutOutPutDto.class)))
    @ApiResponse(responseCode = "403",
            description = "Invalid SKU: must have 11 digits.Negative stock quantity is not allowed.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Product SKU does not exist", content = @Content)
    public ResponseEntity<HttpStatus> updateProduct(@RequestBody @Valid @NonNull final UpdateProductIntPutDto productInPutDto) {
        service.update(productInPutDto);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    @Operation(summary = "Retrieve paginated list of products filtered by criteria.")
    @ApiResponse(responseCode = "200", description = "Success on found products",
            content = @Content(schema = @Schema(implementation = ProductOutPutDto.class)))
    public ResponseEntity<Page<ProductOutPutDto>> listProducts(@ModelAttribute final ProductSearchCriteria searchCriteria,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {

        final var pageable = PageRequest.of(page, size);
        Page<ProductOutPutDto> products = service.getFilteredProducts(searchCriteria, pageable);
        return ResponseEntity.ok(products);
    }

}
