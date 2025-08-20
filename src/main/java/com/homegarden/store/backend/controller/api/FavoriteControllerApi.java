package com.homegarden.store.backend.controller.api;

import com.homegarden.store.backend.dto.FavoriteDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Favorite", description = "Manage current user's favorite products")
public interface FavoriteControllerApi {

    @Operation(
            summary = "Get all favorite products of current user",
            description = "Returns a list of favorites for the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of favorite products",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FavoriteDto.class),
                            examples = @ExampleObject(
                                    name = "Favorites list",
                                    value = """
                                            [
                                              { "userId": 1, "productId": 5 },
                                              { "userId": 1, "productId": 6 }
                                            ]
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Invalid session\"}"))),
            @ApiResponse(responseCode = "404", description = "No favorites found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"No favorite products found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict while retrieving favorites",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Unexpected conflict while retrieving favorites\"}")))
    })
    ResponseEntity<List<FavoriteDto>> getAll();

    @Operation(
            summary = "Add product to favorites",
            description = "Adds a product to the authenticated user's favorites",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FavoriteDto.class),
                            examples = @ExampleObject(
                                    name = "Add to favorites payload",
                                    value = """
                                            { "userId": 1, "productId": 5 }
                                            """
                            ))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product added to favorites",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\":\"Added to favorites\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"userId and productId are required\"}"))),
            @ApiResponse(responseCode = "404", description = "User or product not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Product with id 99 not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Product already in favorites",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Product already added to favorites\"}")))
    })
    ResponseEntity<Void> addToFavorites(@Valid @RequestBody FavoriteDto favoriteDto);

    @Operation(
            summary = "Remove product from favorites",
            description = "Removes a product from the authenticated user's favorites",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FavoriteDto.class),
                            examples = @ExampleObject(
                                    name = "Remove from favorites payload",
                                    value = """
                                            { "userId": 1, "productId": 5 }
                                            """
                            ))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product removed from favorites"),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Invalid productId\"}"))),
            @ApiResponse(responseCode = "404", description = "Favorite not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Favorite entry not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict while removing from favorites",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Could not remove due to constraint\"}")))
    })
    ResponseEntity<Void> removeFromFavorites(@Valid @RequestBody FavoriteDto favoriteDto);
}
