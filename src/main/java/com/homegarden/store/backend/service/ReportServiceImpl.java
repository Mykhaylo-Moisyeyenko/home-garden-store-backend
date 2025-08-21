package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.OrderResponseDto;
import com.homegarden.store.backend.dto.ProfitReportDto;
import com.homegarden.store.backend.dto.TopCancelledProductsReportDto;
import com.homegarden.store.backend.dto.TopTenSelledProductsReportDto;
import com.homegarden.store.backend.entity.Order;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final OrderItemService orderItemService;
    private final OrderService orderService;
    private final EntityManager entityManager;

    @Override
    public List<TopCancelledProductsReportDto> getTopCancelledProducts() {
        List<Object[]> data = orderItemService.getTopCancelledProducts();

        return data.stream()
                .map(obj -> new TopCancelledProductsReportDto(
                        (Long) obj[0],
                        (String) obj[1],
                        (Long) obj[2]
                ))
                .toList();
    }

    @Override
    public List<ProfitReportDto> getProfitReport(LocalDate startDate, LocalDate endDate, String groupBy) {
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.plusDays(1).atStartOfDay();

        List<Object[]> rowsFromDb = orderService.getGroupedRevenue(startTime, endTime, groupBy);

        return rowsFromDb.stream()
                .map(row -> new ProfitReportDto(
                        ((Timestamp) row[0]).toLocalDateTime(),
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    @Override
    public List<TopTenSelledProductsReportDto> getTopTenSelledProducts(String sortBy) {
        String sortColumnBy = sortBy.equals("quantity") ? "totalQuantity" : "totalSum";

        String query = """
                SELECT  oi.product_id,
                        SUM(oi.quantity) AS totalQuantity,
                        SUM(oi.quantity * oi.price_at_purchase) AS totalSum
                FROM order_items oi
                JOIN orders o
                    ON oi.order_id = o.order_id
                WHERE o.status = 'DELIVERED'
                GROUP BY oi.product_id
                ORDER BY %s DESC
                LIMIT 10
                """.formatted(sortColumnBy);

        @SuppressWarnings("unchecked")
        List<Object[]> rowsFromDb = entityManager.createNativeQuery(query).getResultList();

        return rowsFromDb.stream()
                .map(row -> new TopTenSelledProductsReportDto(
                        ((Number) row[0]).longValue(),
                        ((Number) row[1]).longValue(),
                        (BigDecimal) row[2]
                ))
                .toList();
    }

    public List<Order> getAwaitingPaymentOrders(int days) {

        String query = """
                SELECT o.*
                FROM orders o
                JOIN order_items oi
                    ON o.order_id = oi.order_id
                WHERE o.status = 'AWAITING_PAYMENT' AND o.updated_at < NOW() - INTERVAL '%s day'
                ORDER BY o.updated_at ASC
                """.formatted(days);

        List<Order> orders = entityManager
                .createNativeQuery(query, Order.class)
                .getResultList();

        return orders;
    }
}