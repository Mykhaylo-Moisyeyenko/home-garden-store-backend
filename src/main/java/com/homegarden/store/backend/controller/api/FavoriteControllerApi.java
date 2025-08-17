package com.homegarden.store.backend.controller.api;

import com.homegarden.store.backend.dto.FavoriteDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Favorite", description = "Manage user's favorite products")
@RequestMapping("/v1/favorites")
public interface FavoriteControllerApi {

    @Operation(summary = "Get all favorite products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of favorite products",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[{\"userId\":1,\"productId\":5},{\"userId\":1,\"productId\":6}]"))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Invalid user session\"}"))),
            @ApiResponse(responseCode = "404", description = "Favorites not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"No favorite products found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict while retrieving favorites",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"User session conflict\"}")))
    })
    @GetMapping
    ResponseEntity<List<FavoriteDto>> getAll();

    @Operation(summary = "Add product to favorites")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product added to favorites"),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"UserId and ProductId are required\"}"))),
            @ApiResponse(responseCode = "404", description = "User or product not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Product with id 99 not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Product already in favorites",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Product already added to favorites\"}")))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"userId\":1,\"productId\":5}"))
    )
    @PostMapping
    ResponseEntity<Void> addToFavorites(@Valid @RequestBody FavoriteDto favoriteDto);

    @Operation(summary = "Remove product from favorites")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product removed from favorites"),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Invalid product ID\"}"))),
            @ApiResponse(responseCode = "404", description = "Favorite not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Favorite entry not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict while removing from favorites",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Product could not be removed from favorites due to constraint\"}")))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"userId\":1,\"productId\":5}"))
    )
    @DeleteMapping
    ResponseEntity<Void> removeFromFavorites(@Valid @RequestBody FavoriteDto favoriteDto);
}
