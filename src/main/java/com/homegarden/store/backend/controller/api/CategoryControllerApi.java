package com.homegarden.store.backend.controller.api;

import com.homegarden.store.backend.dto.CategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category", description = "Operations related to product categories")
@RequestMapping("/v1/categories")
public interface CategoryControllerApi {

    @Operation(summary = "Create a new category")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"categoryId\":1,\"name\":\"Flowers\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Category name must not be blank\"}"))),
            @ApiResponse(responseCode = "404", description = "Related resource not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Parent category not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict - duplicate category",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Category 'Flowers' already exists\"}")))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"name\":\"Flowers\"}"))
    )
    @PostMapping
    ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto dto);

    @Operation(summary = "Get all categories")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of categories",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[{\"categoryId\":1,\"name\":\"Flowers\"},{\"categoryId\":2,\"name\":\"Garden Tools\"}]"))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Invalid pagination parameters\"}"))),
            @ApiResponse(responseCode = "404", description = "No categories found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"No categories available\"}"))),
            @ApiResponse(responseCode = "409", description = "Data conflict",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Unexpected category conflict\"}")))
    })
    @GetMapping
    ResponseEntity<List<CategoryDto>> getAll();

    @Operation(summary = "Get category by ID", description = "Returns a category by its unique Category ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"categoryId\":1,\"name\":\"Flowers\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"ID must be greater than 0\"}"))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Category with id 10 not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Data conflict",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Conflict while retrieving category\"}")))
    })
    @GetMapping("/{categoryId}")
    ResponseEntity<CategoryDto> getById(@PathVariable("categoryId") @Min(1) Long categoryId);

    @Operation(summary = "Delete category by ID", description = "Deletes a category by its unique Category ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"ID must be greater than 0\"}"))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Category with id 10 not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict during deletion",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Category is linked to products\"}")))
    })
    @DeleteMapping("/{categoryId}")
    ResponseEntity<Void> delete(@PathVariable("categoryId") @Min(1) Long categoryId);

    @Operation(summary = "Update category by ID", description = "Updates a category by its unique Category ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"categoryId\":1,\"name\":\"Updated Name\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Category name can't be empty\"}"))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Category with id 10 not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Update conflict",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Cannot rename to existing category 'Flowers'\"}")))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"name\":\"Updated Name\"}"))
    )
    @PutMapping("/{categoryId}")
    ResponseEntity<CategoryDto> updateCategory(@PathVariable("categoryId") @Min(1) Long categoryId,
                                               @Valid @RequestBody CategoryDto dto);
}
