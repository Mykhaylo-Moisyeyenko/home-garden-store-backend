package com.homegarden.store.backend.controller.api;

import com.homegarden.store.backend.dto.CreateProductDto;
import com.homegarden.store.backend.dto.ProductDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Products", description = "Operations related to managing garden store products")
public interface ProductControllerApi {

    @Operation(
            summary = "Create a new product",
            description = "Creates a new product and returns it",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateProductDto.class),
                            examples = @ExampleObject(
                                    name = "CreateProductDto example",
                                    value = """
                                            {
                                              "name": "Orchid Pot",
                                              "description": "Ceramic pot for indoor plants",
                                              "price": 12.99,
                                              "categoryId": 3,
                                              "imageUrl": "https://images.gardenstore.com/pots/orchid.jpg",
                                              "discountPrice": 10.99
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class),
                            examples = @ExampleObject(
                                    name = "Created product",
                                    value = """
                                            {
                                              "productId": 1,
                                              "name": "Orchid Pot",
                                              "description": "Ceramic pot for indoor plants",
                                              "price": 12.99,
                                              "categoryId": 3,
                                              "imageUrl": "https://images.gardenstore.com/pots/orchid.jpg",
                                              "discountPrice": 10.99,
                                              "createdAt": "2025-08-01T10:15:30.000+00:00",
                                              "updatedAt": "2025-08-01T10:15:30.000+00:00"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Missing required fields: name, price\"}"))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Category with id 3 not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Product already exists",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Product with name 'Orchid Pot' already exists\"}")))
    })
    ResponseEntity<ProductDto> create(@RequestBody @Valid CreateProductDto productDto);

    @Operation(
            summary = "Get all products",
            description = "Returns all products, with optional filtering by category, price range, discount and sort"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of products",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class),
                            examples = @ExampleObject(
                                    name = "Products list",
                                    value = """
                                            [
                                              {
                                                "productId": 1,
                                                "name": "Orchid Pot",
                                                "description": "Ceramic pot for indoor plants",
                                                "price": 12.99,
                                                "categoryId": 3,
                                                "imageUrl": "https://images.gardenstore.com/pots/orchid.jpg",
                                                "discountPrice": 10.99,
                                                "createdAt": "2025-08-01T10:15:30.000+00:00",
                                                "updatedAt": "2025-08-01T10:15:30.000+00:00"
                                              },
                                              {
                                                "productId": 2,
                                                "name": "Sunflower Seeds",
                                                "description": "High quality seeds",
                                                "price": 3.99,
                                                "categoryId": 5,
                                                "imageUrl": "https://images.gardenstore.com/seeds/sunflower.jpg",
                                                "discountPrice": 3.49,
                                                "createdAt": "2025-08-01T11:00:00.000+00:00",
                                                "updatedAt": "2025-08-01T11:00:00.000+00:00"
                                              }
                                            ]
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid filter parameters",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Invalid price range: minPrice must be <= maxPrice\"}"))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Category not found with id: 3\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflicting filters",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Discount cannot be combined with sort by price DESC\"}")))
    })
    ResponseEntity<List<ProductDto>> getAll(
            @Parameter(description = "Filter by category id", example = "3")
            @Min(1) Long categoryId,
            @Parameter(description = "Minimal price", example = "1.00")
            @PositiveOrZero BigDecimal minPrice,
            @Parameter(description = "Maximal price", example = "100.00")
            @Positive BigDecimal maxPrice,
            @Parameter(description = "Only discounted products", example = "true")
            Boolean discount,
            @Parameter(description = "Sort by price ASC or DESC", example = "ASC")
            @Pattern(regexp = "ASC|DESC") String sort
    );

    @Operation(
            summary = "Get product by ID",
            description = "Finds product by its unique Product ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class),
                            examples = @ExampleObject(
                                    name = "Product example",
                                    value = """
                                            {
                                              "productId": 1,
                                              "name": "Orchid Pot",
                                              "description": "Ceramic pot for indoor plants",
                                              "price": 12.99,
                                              "categoryId": 3,
                                              "imageUrl": "https://images.gardenstore.com/pots/orchid.jpg",
                                              "discountPrice": 10.99,
                                              "createdAt": "2025-08-01T10:15:30.000+00:00",
                                              "updatedAt": "2025-08-01T10:15:30.000+00:00"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid Product ID",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Product ID must be a positive number\"}"))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Product not found with id: 99\"}"))),
            @ApiResponse(responseCode = "409", description = "Multiple products found with same ID",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Database inconsistency detected for product ID: 99\"}")))
    })
    ResponseEntity<ProductDto> getById(
            @Parameter(description = "Product ID", example = "1")
            @PathVariable("id") @Min(1) Long productId
    );

    @Operation(
            summary = "Update product",
            description = "Updates a product by its unique Product ID",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateProductDto.class),
                            examples = @ExampleObject(
                                    name = "Update payload",
                                    value = """
                                            {
                                              "name": "Updated Orchid Pot",
                                              "description": "Updated description",
                                              "price": 14.99,
                                              "categoryId": 3,
                                              "imageUrl": "https://images.gardenstore.com/pots/orchid-new.jpg",
                                              "discountPrice": 12.99
                                            }
                                            """
                            ))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class),
                            examples = @ExampleObject(
                                    name = "Updated product",
                                    value = """
                                            {
                                              "productId": 1,
                                              "name": "Updated Orchid Pot",
                                              "description": "Updated description",
                                              "price": 14.99,
                                              "categoryId": 3,
                                              "imageUrl": "https://images.gardenstore.com/pots/orchid-new.jpg",
                                              "discountPrice": 12.99,
                                              "createdAt": "2025-08-01T10:15:30.000+00:00",
                                              "updatedAt": "2025-08-02T09:00:00.000+00:00"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid update data",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Name cannot be empty\"}"))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Product not found with id: 5\"}"))),
            @ApiResponse(responseCode = "409", description = "Duplicate product name",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Product with name already exists\"}")))
    })
    ResponseEntity<ProductDto> updateProduct(
            @Parameter(description = "Product ID", example = "1")
            @PathVariable("id") @Min(1) Long productId,
            @RequestBody @Valid CreateProductDto productDto
    );

    @Operation(
            summary = "Delete product",
            description = "Deletes a product by its unique Product ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID provided",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"ID must be greater than 0\"}"))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Product not found with id: 77\"}"))),
            @ApiResponse(responseCode = "409", description = "Product used in existing orders",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Product cannot be deleted, it is used in orders\"}")))
    })
    ResponseEntity<Void> delete(
            @Parameter(description = "Product ID", example = "1")
            @PathVariable("id") @Min(1) Long productId
    );

    @Operation(
            summary = "Set discount price",
            description = "Updates the discount price for a given Product ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Discount price updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class),
                            examples = @ExampleObject(
                                    name = "Discount updated",
                                    value = """
                                            {
                                              "productId": 1,
                                              "name": "Orchid Pot",
                                              "description": "Ceramic pot for indoor plants",
                                              "price": 12.99,
                                              "categoryId": 3,
                                              "imageUrl": "https://images.gardenstore.com/pots/orchid.jpg",
                                              "discountPrice": 9.99,
                                              "createdAt": "2025-08-01T10:15:30.000+00:00",
                                              "updatedAt": "2025-08-02T09:10:00.000+00:00"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid discount price",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Discount price must be lower than original price\"}"))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Product not found with id: 22\"}"))),
            @ApiResponse(responseCode = "409", description = "Discount already applied",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Product already has this discount price\"}")))
    })
    ResponseEntity<ProductDto> setDiscountPrice(
            @Parameter(description = "Product ID", example = "1") @Min(1) Long productId,
            @Parameter(description = "New discount price", example = "9.99") @Positive BigDecimal newDiscountPrice
    );

    @Operation(
            summary = "Get product of the day",
            description = "Returns the product with the best discount offer"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product of the day returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class),
                            examples = @ExampleObject(
                                    name = "Product of the day",
                                    value = """
                                            {
                                              "productId": 2,
                                              "name": "Sunflower Seeds",
                                              "description": "High quality seeds",
                                              "price": 3.99,
                                              "categoryId": 5,
                                              "imageUrl": "https://images.gardenstore.com/seeds/sunflower.jpg",
                                              "discountPrice": 3.49,
                                              "createdAt": "2025-08-01T11:00:00.000+00:00",
                                              "updatedAt": "2025-08-01T11:00:00.000+00:00"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "Unexpected processing error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Computation error",
                                    value = "{\"error\":\"Error calculating product of the day\"}"
                            ))),
            @ApiResponse(responseCode = "404", description = "No discounted product found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "No discounts",
                                    value = "{\"error\":\"No products with discount found\"}"
                            ))),
            @ApiResponse(responseCode = "409", description = "Multiple top discounts found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ambiguous result",
                                    value = "{\"error\":\"Multiple products have the same top discount\"}"
                            )))
    })
    ResponseEntity<ProductDto> getProductOfTheDay();
}
