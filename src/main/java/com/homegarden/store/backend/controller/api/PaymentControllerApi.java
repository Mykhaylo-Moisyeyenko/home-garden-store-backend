package com.homegarden.store.backend.controller.api;

import com.homegarden.store.backend.dto.PaymentCreateDto;
import com.homegarden.store.backend.dto.PaymentResponseDto;
import com.homegarden.store.backend.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Payments", description = "Operations related to payment processing")
public interface PaymentControllerApi {

    @Operation(
            summary = "Get all payments",
            description = "Retrieve all payments in the system"
    )
    @Parameters({
            @Parameter(name = "page", description = "Page number (0..N)", example = "0"),
            @Parameter(name = "size", description = "Page size", example = "20")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponseDto.class),
                            examples = @ExampleObject(name = "Payments",
                                    value = """
                                            [
                                              {
                                                "paymentId": 1,
                                                "orderId": 42,
                                                "amount": 3500.00,
                                                "status": "PENDING",
                                                "createdAt": "2025-08-18T10:15:30",
                                                "updatedAt": null
                                              }
                                            ]
                                            """))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Invalid pagination",
                                    value = "{\"error\": \"Invalid pagination parameters: page must be >= 0\"}"))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "No payments",
                                    value = "{\"error\": \"No payments found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Conflict",
                                    value = "{\"error\": \"Conflict while retrieving payments\"}")))
    })    
    ResponseEntity<List<PaymentResponseDto>> getAllPayments();

    @Operation(
            summary = "Create a new payment",
            description = "Create a new payment for an order",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentCreateDto.class),
                            examples = @ExampleObject(name = "CreatePayment",
                                    value = "{\"orderId\": 42}"))
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponseDto.class),
                            examples = @ExampleObject(name = "Created",
                                    value = "{\"paymentId\": 10, \"orderId\": 42, \"amount\": 3500.00, \"status\": \"PENDING\", \"createdAt\": \"2025-08-18T10:20:00\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Validation error",
                                    value = "{\"error\": \"Order ID must be more than 0\"}"))),
            @ApiResponse(responseCode = "404", description = "Order not found / wrong state",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Order not acceptable",
                                    value = "{\"error\": \"Unable create payment for order\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict (duplicate unpaid payment)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Duplicate",
                                    value = "{\"error\": \"Order id 42 already has unpaid payment\"}")))
    })
    ResponseEntity<PaymentResponseDto> create(@RequestBody @Valid PaymentCreateDto dto);

    @Operation(
            summary = "Confirm a payment",
            description = "Confirm an existing payment by ID"
    )
    @Parameters({
            @Parameter(name = "paymentId", description = "Payment ID", example = "10"),
            @Parameter(name = "status", description = "New payment status",
                    schema = @Schema(implementation = PaymentStatus.class), example = "SUCCESS")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment confirmed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponseDto.class),
                            examples = @ExampleObject(name = "Confirmed",
                                    value = "{\"paymentId\": 10, \"orderId\": 42, \"amount\": 3500.00, \"status\": \"SUCCESS\", \"updatedAt\": \"2025-08-18T10:25:00\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Invalid status",
                                    value = "{\"error\": \"Unsupported payment status\"}"))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Payment not found",
                                    value = "{\"error\": \"Payment with id 10 not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Cannot confirm",
                                    value = "{\"error\": \"Cannot confirm payment in current state\"}")))
    })
    ResponseEntity<PaymentResponseDto> confirm(@PathVariable @Min(1) Long paymentId,
                                               @RequestParam(defaultValue = "SUCCESS") PaymentStatus status);

    @Operation(
            summary = "Get payment by ID",
            description = "Retrieve payment information by ID"
    )
    @Parameters({
            @Parameter(name = "paymentId", description = "Payment ID", example = "10")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponseDto.class),
                            examples = @ExampleObject(name = "Payment",
                                    value = "{\"paymentId\": 10, \"orderId\": 42, \"amount\": 3500.00, \"status\": \"PENDING\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Invalid ID",
                                    value = "{\"error\": \"ID must be greater than 0\"}"))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Not found",
                                    value = "{\"error\": \"Payment with id 10 not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Conflict",
                                    value = "{\"error\": \"Conflict while fetching payment\"}")))
    })
    ResponseEntity<PaymentResponseDto> getById(@PathVariable("paymentId") @Min(1) Long paymentId);

    @Operation(
            summary = "Get payments by Order ID",
            description = "Retrieve all payments associated with a given order"
    )
    @Parameters({
            @Parameter(name = "orderId", description = "Order ID", example = "42")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponseDto.class),
                            examples = @ExampleObject(name = "Order payments",
                                    value = """
                                            [
                                              {
                                                "paymentId": 11,
                                                "orderId": 42,
                                                "amount": 1200.00,
                                                "status": "SUCCESS"
                                              },
                                              {
                                                "paymentId": 12,
                                                "orderId": 42,
                                                "amount": 2300.00,
                                                "status": "PENDING"
                                              }
                                            ]
                                            """))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Invalid order ID",
                                    value = "{\"error\": \"Order ID must be greater than 0\"}"))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Not found",
                                    value = "{\"error\": \"Order with id 42 not found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Conflict",
                                    value = "{\"error\": \"Conflict while fetching payments by order\"}")))
    })
    ResponseEntity<List<PaymentResponseDto>> getPaymentsByOrder(@PathVariable("orderId") @Min(1) Long orderId);
}
