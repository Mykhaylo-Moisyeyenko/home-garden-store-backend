package com.homegarden.store.backend.controller.api;

import com.homegarden.store.backend.dto.CartItemResponseDto;
import com.homegarden.store.backend.dto.CartResponseDto;
import com.homegarden.store.backend.dto.CreateCartItemRequestDto;
import com.homegarden.store.backend.dto.UpdateCartItemRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Carts", description = "Operations with user's shopping cart")
@RequestMapping("v1/carts")
public interface CartControllerApi {

    @Operation(summary = "Get all cart items", description = "Return all items in the current user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart items found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartItemResponseDto.class),
                            examples = @ExampleObject(name = "Items",
                                    value = """
                                            [
                                              { "productId": 101, 
                                              "productName": "Garden Shovel", 
                                              "quantity": 2 },
                                              { "productId": 202, 
                                              "productName": "Terracotta Pot", 
                                              "quantity": 4 }
                                            ]
                                            """))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Invalid context",
                                    value = "{\"error\": \"Invalid request context\"}"))),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "No cart",
                                    value = "{\"error\": \"Cart for current user not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Conflict",
                                    value = "{\"error\": \"Conflict while fetching cart items\"}")))
    })
    @GetMapping
    List<CartItemResponseDto> getAllCartItems();

    @Operation(summary = "Delete cart", description = "Delete the current user's cart with all items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart deleted successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Invalid state",
                                    value = "{\"error\": \"Cannot delete cart in current state\"}"))),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "No cart",
                                    value = "{\"error\": \"Cart for current user not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Conflict",
                                    value = "{\"error\": \"Conflict while deleting cart\"}")))
    })
    @DeleteMapping
    ResponseEntity<Void> delete();

    @Operation(summary = "Add item to cart",
            description = "Add a product to the current user's cart",
            requestBody = @RequestBody(required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateCartItemRequestDto.class),
                            examples = @ExampleObject(name = "AddItem",
                                    value = "{\"productId\": 101, \"quantity\": 2}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item added",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartResponseDto.class),
                            examples = @ExampleObject(name = "Cart",
                                    value = """
                                            { "cartId": 77, 
                                            "userId": 5,
                                              "items": [
                                                { "productId": 101, 
                                                "productName": "Garden Shovel", 
                                                "quantity": 2 }
                                              ]
                                            }
                                            """))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Validation error",
                                    value = "{\"error\": \"productId and quantity must be provided\"}"))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "No product",
                                    value = "{\"error\": \"Product with id 101 not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Cannot add",
                                    value = "{\"error\": \"Cannot add item to cart\"}")))
    })
    @PostMapping("item")
    ResponseEntity<CartResponseDto> addCartItem(@RequestBody @Valid @NotNull CreateCartItemRequestDto dto);

    @Operation(summary = "Update cart item quantity",
            description = "Update quantity for a specific cart item",
            requestBody = @RequestBody(required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateCartItemRequestDto.class),
                            examples = @ExampleObject(name = "UpdateItem",
                                    value = "{\"cartItemId\": 555, \"quantity\": 3}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartResponseDto.class),
                            examples = @ExampleObject(name = "Cart",
                                    value = """
                                        { "cartId": 77, "userId": 5,
                                          "items": [
                                            { "productId": 101, 
                                            "productName": "Garden Shovel", 
                                            "quantity": 3 }
                                          ]
                                        }
                                        """))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Validation error",
                                    value = "{\"error\": \"cartItemId must be > 0, quantity must be >= 0\"}"))),
            @ApiResponse(responseCode = "404", description = "Cart item not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Not found",
                                    value = "{\"error\": \"Cart item with id 555 not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Cannot update",
                                    value = "{\"error\": \"Cannot update item in current state\"}")))
    })
    @PutMapping("item")
    ResponseEntity<CartResponseDto> updateCartItemQuantity(@RequestBody @Valid @NotNull UpdateCartItemRequestDto dto);

    @Operation(summary = "Delete cart item", description = "Delete a specific cart item by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartResponseDto.class),
                            examples = @ExampleObject(name = "Cart",
                                    value = """
                                            { "cartId": 77, 
                                            "userId": 5,
                                              "items": []
                                            }
                                            """))),
            @ApiResponse(responseCode = "400", description = "Invalid ID",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Invalid id",
                                    value = "{\"error\": \"Cart Item ID must be > 0\"}"))),
            @ApiResponse(responseCode = "404", description = "Cart item not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Not found",
                                    value = "{\"error\": \"Cart item with id 555 not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Cannot delete",
                                    value = "{\"error\": \"Cannot delete item in current state\"}")))
    })
    @DeleteMapping("item/{id}")
    ResponseEntity<CartResponseDto> deleteCartItem(@PathVariable("id") @NotNull @Min(1) Long cartItemId);
}

