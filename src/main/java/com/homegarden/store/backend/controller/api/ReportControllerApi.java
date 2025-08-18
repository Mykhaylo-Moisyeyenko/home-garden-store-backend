package com.homegarden.store.backend.controller.api;

import com.homegarden.store.backend.dto.OrderResponseDto;
import com.homegarden.store.backend.dto.ProfitReportDto;
import com.homegarden.store.backend.dto.TopCancelledProductsReportDto;
import com.homegarden.store.backend.dto.TopTenSelledProductsReportDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Reports", description = "Endpoints for analytical and financial reporting")
@RequestMapping("v1/reports")
public interface ReportControllerApi {

    @Operation(summary = "Get top cancelled products", description = "Returns products most frequently cancelled by users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top cancelled products returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TopCancelledProductsReportDto.class),
                            examples = @ExampleObject(name = "TopCancelledProducts", value = """
                                    [
                                      {
                                        "productId": 17,
                                        "productName": "Garden Shovel",
                                        "cancelCount": 12
                                      }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Invalid request", value = "{\"error\": \"Invalid request parameters\"}"))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "No cancelled products", value = "{\"error\": \"No cancelled products found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Conflict", value = "{\"error\": \"Unable to generate top cancelled products report\"}")))
    })
    @GetMapping("top-cancelled-products")
    ResponseEntity<List<TopCancelledProductsReportDto>> getTopCancelledProducts();

    @Operation(summary = "Get profit report", description = "Returns profit report for a given date range")
    @Parameters({
            @Parameter(name = "startDate", description = "Start date of report", example = "2025-01-01"),
            @Parameter(name = "endDate", description = "End date of report", example = "2025-08-01"),
            @Parameter(name = "groupBy", description = "Grouping criteria: day | month | year", example = "month")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profit report generated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfitReportDto.class),
                            examples = @ExampleObject(name = "ProfitReport", value = """
                                    [
                                      {
                                        "period": "2025-07",
                                        "totalProfit": 15230.50
                                      }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Invalid date range", value = "{\"error\": \"Start date must be before end date\"}"))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "No data", value = "{\"error\": \"No profit data for given range\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict while generating report",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Conflict", value = "{\"error\": \"Unable to compute profit report\"}")))
    })
    @GetMapping("profit-report")
    ResponseEntity<List<ProfitReportDto>> getProfitReport(
            @RequestParam @PastOrPresent @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @PastOrPresent @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam String groupBy);

    @Operation(summary = "Get top ten sold products", description = "Returns top 10 best-selling products")
    @Parameters({
            @Parameter(name = "sortBy", description = "Sorting criteria: quantity | sum", example = "quantity")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top sold products returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TopTenSelledProductsReportDto.class),
                            examples = @ExampleObject(name = "TopTenSold", value = """
                                    [
                                      {
                                        "productId": 5,
                                        "productName": "Terracotta Pot",
                                        "quantity": 120,
                                        "totalSum": 4800.00
                                      }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid sort criteria",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Invalid sortBy", value = "{\"error\": \"sortBy must be quantity or sum\"}"))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "No products", value = "{\"error\": \"No sold products found\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Conflict", value = "{\"error\": \"Unable to generate top products report\"}")))
    })
    @GetMapping("top-ten-selled-products")
    ResponseEntity<List<TopTenSelledProductsReportDto>> getTopTenSelledProducts(@RequestParam String sortBy);

    @Operation(summary = "Get orders awaiting payment", description = "Returns list of orders not yet paid after given number of days")
    @Parameters({
            @Parameter(name = "days", description = "Number of days passed since order was placed", example = "5")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class),
                            examples = @ExampleObject(name = "AwaitingPayments", value = """
                                    [
                                      {
                                        "orderId": 31,
                                        "userId": 3,
                                        "status": "CREATED",
                                        "orderTotalSum": 5700.00,
                                        "createdAt": "2025-08-10"
                                      }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid day count",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Invalid days", value = "{\"error\": \"days must be a positive integer\"}"))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "No awaiting orders", value = "{\"error\": \"No orders awaiting payment\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict while fetching orders",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Conflict", value = "{\"error\": \"Unable to retrieve orders awaiting payment\"}")))
    })
    @GetMapping("orders-awaiting-payment")
    ResponseEntity<List<OrderResponseDto>> getOrdersAwaitingPayment(@RequestParam @Positive int days);
}
