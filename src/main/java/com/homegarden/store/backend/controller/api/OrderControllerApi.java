package com.homegarden.store.backend.controller.api;

import com.homegarden.store.backend.dto.CreateOrderRequestDto;
import com.homegarden.store.backend.dto.OrderResponseDto;
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

@Tag(name = "Orders", description = "Operations related to orders")
@RequestMapping("/v1/orders")

public interface OrderControllerApi {

    @Operation(summary = "Create a new order", description = "Allows a user to create a new order",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateOrderRequestDto.class),
                            examples = @ExampleObject(name = "New Order", value = "{\"userId\": 1, \"productIds\": [1, 2], \"totalPrice\": 3000}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class),
                            examples = @ExampleObject(name = "Created Order", value = "{\"id\": 1, \"totalPrice\": 3000.0, \"status\": \"CREATED\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(examples = @ExampleObject(value = "Invalid order data"))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(examples = @ExampleObject(value = "User not found"))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(examples = @ExampleObject(value = "Order conflict occurred")))
    })
    @PostMapping
    ResponseEntity<OrderResponseDto> create(@RequestBody @Valid @NotNull CreateOrderRequestDto orderRequestDTO);

    @Operation(summary = "Get all orders", description = "Retrieve all orders placed in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class),
                            examples = @ExampleObject(name = "Order List", value = "[{\"id\": 1, \"totalPrice\": 3000.0, \"status\": \"CREATED\"}]"))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(examples = @ExampleObject(value = "Invalid query parameters"))),
            @ApiResponse(responseCode = "404", description = "No orders found", content = @Content(examples = @ExampleObject(value = "No orders in system"))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(examples = @ExampleObject(value = "Conflict while fetching orders")))
    })
    @GetMapping
    ResponseEntity<List<OrderResponseDto>> getAll();

    @Operation(summary = "Get order by ID", description = "Retrieve a single order using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class),
                            examples = @ExampleObject(name = "Single Order", value = "{\"id\": 1, \"totalPrice\": 3000.0, \"status\": \"SHIPPED\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid ID", content = @Content(examples = @ExampleObject(value = "ID must be greater than 0"))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(examples = @ExampleObject(value = "Order with this ID not found"))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(examples = @ExampleObject(value = "Conflict while fetching order")))
    })
    @GetMapping("/{orderId}")
    ResponseEntity<OrderResponseDto> getById(@PathVariable @Min(1) Long orderId);

    @Operation(summary = "Get all orders of current user", description = "Retrieve all orders placed by the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class),
                            examples = @ExampleObject(name = "User Orders", value = "[{\"id\": 2, \"totalPrice\": 1200.0, \"status\": \"PAID\"}]"))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(examples = @ExampleObject(value = "Invalid request context"))),
            @ApiResponse(responseCode = "404", description = "No orders found", content = @Content(examples = @ExampleObject(value = "User has no orders"))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(examples = @ExampleObject(value = "Conflict while fetching user orders")))
    })
    @GetMapping("/history")
    ResponseEntity<List<OrderResponseDto>> getAllByUser();

    @Operation(summary = "Cancel an order", description = "Allows a user to cancel an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order cancelled"),
            @ApiResponse(responseCode = "400", description = "Invalid ID", content = @Content(examples = @ExampleObject(value = "ID must be greater than 0"))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(examples = @ExampleObject(value = "Order with this ID not found"))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(examples = @ExampleObject(value = "Cannot cancel this order")))
    })
    @PatchMapping("/{orderId}/cancel")
    ResponseEntity<Void> cancel(@PathVariable @Min(1) Long orderId);
}
