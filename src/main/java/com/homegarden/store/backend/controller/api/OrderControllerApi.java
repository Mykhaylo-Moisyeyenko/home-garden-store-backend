package com.homegarden.store.backend.controller.api;

import com.homegarden.store.backend.dto.CreateOrderRequestDto;
import com.homegarden.store.backend.dto.OrderResponseDto;
import com.homegarden.store.backend.enums.Status;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Tag(name = "Order", description = "Operations related to orders")
public interface OrderControllerApi {

    @Operation(
            summary = "Create an order",
            description = "Creates a new order for the current user from their cart",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateOrderRequestDto.class),
                            examples = @ExampleObject(
                                    name = "Create order payload",
                                    value = """
                                            {
                                              "orderItems": [
                                                { "productId": 5, "quantity": 2 },
                                                { "productId": 6, "quantity": 1 }
                                              ],
                                              "deliveryAddress": "Berlin, Unter den Linden 1",
                                              "deliveryMethod": "COURIER"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Created order",
                                    value = """
                                            {
                                              "orderId": 101,
                                              "status": "CREATED",
                                              "orderTotalSum": 29.97,
                                              "deliveryAddress": "Berlin, Unter den Linden 1",
                                              "deliveryMethod": "COURIER",
                                              "contactPhone": "+49-151-000000",
                                              "items": [
                                                { "productId": 5, "quantity": 2, "priceAtPurchase": 9.99 },
                                                { "productId": 6, "quantity": 1, "priceAtPurchase": 9.99 }
                                              ],
                                              "createdAt": "2025-08-20T10:10:00",
                                              "updatedAt": "2025-08-20T10:10:00"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid input / empty orderItems",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Products must be in the cart\"}"))),
            @ApiResponse(responseCode = "404", description = "Product not found / cart not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Product with id 999 not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict while creating order",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Unable to create order due to conflicting state\"}")))
    })
    ResponseEntity<OrderResponseDto> create(
            @RequestBody @Valid CreateOrderRequestDto request
    );

    @Operation(
            summary = "Get all orders (ADMIN)",
            description = "Returns all orders (admin only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of orders",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Orders list",
                                    value = """
                                            [
                                              { "orderId": 101, "status": "PAID", "orderTotalSum": 49.98 },
                                              { "orderId": 102, "status": "SHIPPED", "orderTotalSum": 19.99 }
                                            ]
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Invalid filter or paging params\"}"))),
            @ApiResponse(responseCode = "404", description = "No orders found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"No orders available\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict while retrieving orders",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Unexpected conflict while retrieving orders\"}")))
    })
    ResponseEntity<List<OrderResponseDto>> getAll();

    @Operation(
            summary = "Get my orders",
            description = "Returns all orders placed by the current authenticated user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of user's orders",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class),
                            examples = @ExampleObject(
                                    name = "My orders",
                                    value = """
                                            [
                                              { "orderId": 101, "status": "DELIVERED", "orderTotalSum": 29.97 },
                                              { "orderId": 105, "status": "PAID", "orderTotalSum": 14.99 }
                                            ]
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Invalid session or parameters\"}"))),
            @ApiResponse(responseCode = "404", description = "No orders for user",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"No orders for current user\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict while retrieving user's orders",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Unexpected conflict while retrieving user's orders\"}")))
    })
    ResponseEntity<List<OrderResponseDto>> getAllByUser();

    @Operation(
            summary = "Get order by ID (current user)",
            description = "Returns an order by id for the current user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Order example",
                                    value = """
                                            {
                                              "orderId": 101,
                                              "status": "CREATED",
                                              "orderTotalSum": 29.97,
                                              "items": [
                                                { "productId": 5, "quantity": 2, "priceAtPurchase": 9.99 }
                                              ]
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid ID",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"ID must be greater than 0\"}"))),
            @ApiResponse(responseCode = "404", description = "Order not found for current user",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Order with id 101 not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict while retrieving order",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Unexpected order conflict\"}")))
    })
    ResponseEntity<OrderResponseDto> getById(
            @Parameter(description = "Order ID", example = "101")
            @PathVariable("id") @Min(1) Long id
    );

    @Operation(
            summary = "Cancel order",
            description = "Cancels order if its status is CREATED or AWAITING_PAYMENT"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order cancelled",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\":\"Order cancelled\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid ID",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"ID must be greater than 0\"}"))),
            @ApiResponse(responseCode = "404", description = "Order not found for current user",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Order with id 101 not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Order can't be cancelled (wrong status)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Order with id 101 can't be cancelled in status SHIPPED\"}")))
    })
    ResponseEntity<Void> cancel(
            @Parameter(description = "Order ID", example = "101")
            @PathVariable("id") @Min(1) Long id
    );

    @Operation(
            summary = "Get grouped revenue (ADMIN)",
            description = "Revenue grouped by hour/day/week/month in a period"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Revenue series",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Object.class),
                            examples = @ExampleObject(
                                    name = "Revenue list",
                                    value = """
                                            [
                                              { "period": "2025-08-01T00:00:00", "revenue": 199.95 },
                                              { "period": "2025-08-02T00:00:00", "revenue": 349.40 }
                                            ]
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"timeCut must be one of: hour, day, week, month\"}"))),
            @ApiResponse(responseCode = "404", description = "No data in period",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"No revenue data for the selected period\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict while aggregating revenue",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Aggregation failed due to conflicting state\"}")))
    })
    ResponseEntity<List<Map<String, Object>>> getGroupedRevenue(
            @Parameter(description = "Start ISO datetime (inclusive)", example = "2025-08-01T00:00:00")
            @RequestParam(name = "startPeriod") String startPeriod,
            @Parameter(description = "End ISO datetime (exclusive)", example = "2025-08-03T00:00:00")
            @RequestParam(name = "endPeriod") String endPeriod,
            @Parameter(description = "Grouping: hour/day/week/month", example = "day")
            @RequestParam(name = "timeCut") @Pattern(regexp = "hour|day|week|month") String timeCut
    );

    @Operation(
            summary = "Get orders by status updated before time (ADMIN)",
            description = "Returns orders with the given status updated before the provided datetime"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Orders before",
                                    value = """
                                            [
                                              { "orderId": 101, "status": "PAID", "orderTotalSum": 49.98, "updatedAt": "2025-08-20T09:00:00" }
                                            ]
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"updatedAtBefore must be ISO datetime\"}"))),
            @ApiResponse(responseCode = "404", description = "No orders match criteria",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"No PAID orders before 2025-08-20T09:00:00\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict while retrieving orders",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Unexpected conflict while filtering orders\"}")))
    })
    ResponseEntity<List<OrderResponseDto>> getAllByStatusAndUpdatedAtBefore(
            @Parameter(description = "Order status", example = "PAID") @RequestParam Status status,
            @Parameter(description = "ISO datetime", example = "2025-08-20T09:00:00") @RequestParam String updatedAtBefore
    );

    @Operation(
            summary = "Get orders by status updated after time (ADMIN)",
            description = "Returns orders with the given status updated after the provided datetime"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Orders after",
                                    value = """
                                            [
                                              { "orderId": 102, "status": "SHIPPED", "orderTotalSum": 19.99, "updatedAt": "2025-08-20T12:00:01" }
                                            ]
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"updatedAtAfter must be ISO datetime\"}"))),
            @ApiResponse(responseCode = "404", description = "No orders match criteria",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"No SHIPPED orders after 2025-08-20T12:00:00\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict while retrieving orders",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Unexpected conflict while filtering orders\"}")))
    })
    ResponseEntity<List<OrderResponseDto>> getAllByStatusAndUpdatedAtAfter(
            @Parameter(description = "Order status", example = "SHIPPED") @RequestParam Status status,
            @Parameter(description = "ISO datetime", example = "2025-08-20T12:00:00") @RequestParam String updatedAtAfter
    );
}