package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.ProfitReportDto;
import com.homegarden.store.backend.dto.TopCancelledProductsReportDto;
import com.homegarden.store.backend.dto.TopTenSelledProductsReportDto;
import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.enums.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private OrderService orderService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    void getTopCancelledProductsTest() {
        Object[] data = new Object[]{1L, "Product Name", 5L};
        when(orderItemService.getTopCancelledProducts()).thenReturn(List.<Object[]>of(data));

        List<TopCancelledProductsReportDto> result = reportService.getTopCancelledProducts();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).productId()).isEqualTo(1L);
        verify(orderItemService, times(1)).getTopCancelledProducts();
    }

    @Test
    void getProfitReport() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 12, 0, 0, 0);
        String timeCut = "month";

        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2025, 1, 1, 0, 0, 0));
        List<Object[]> expectedRaw = List.<Object[]>of(
                new Object[]{timestamp, BigDecimal.valueOf(2000)}
        );

        ProfitReportDto dto = new ProfitReportDto(LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                BigDecimal.valueOf(2000));
        List<ProfitReportDto> expectedList = List.of(dto);

        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 11);

        when(orderService.getGroupedRevenue(start, end, timeCut)).thenReturn(expectedRaw);

        List<ProfitReportDto> actual = reportService.getProfitReport(startDate, endDate, timeCut);

        assertThat(actual).hasSize(1);
        assertThat(actual).isEqualTo(expectedList);
        verify(orderService).getGroupedRevenue(start, end, timeCut);
    }

    @Test
    void getTopTenSelledProducts() {
        String sortBy = "quantity";
        TopTenSelledProductsReportDto dto1 = new TopTenSelledProductsReportDto(
                1L, 500L, BigDecimal.valueOf(2000.00));
        TopTenSelledProductsReportDto dto2 = new TopTenSelledProductsReportDto(
                2L, 200L, BigDecimal.valueOf(1000.00));
        List<TopTenSelledProductsReportDto> expectedList = List.of(dto1, dto2);

        List<Object[]> expectedRaw = List.<Object[]>of(
                new Object[]{1L, 500L, BigDecimal.valueOf(2000.00)},
                new Object[]{2L, 200L, BigDecimal.valueOf(1000.00)}
        );

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedRaw);

        List<TopTenSelledProductsReportDto> actual = reportService.getTopTenSelledProducts(sortBy);
        assertThat(actual).hasSize(2);
        assertThat(actual).isEqualTo(expectedList);
        verify(entityManager, times(1)).createNativeQuery(anyString());
    }

    @Test
    void getAwaitingPaymentOrders() {
        Order order1 = Order.builder().orderId(1L).status(Status.AWAITING_PAYMENT).build();
        Order order2 = Order.builder().orderId(2L).status(Status.AWAITING_PAYMENT).build();
        List<Order> expectedOrders = List.of(order1, order2);

        when(entityManager.createNativeQuery(anyString(), eq(Order.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedOrders);

        List<Order> actual = reportService.getAwaitingPaymentOrders(5);

        assertThat(actual).hasSize(2);
        assertThat(actual).isEqualTo(expectedOrders);
        verify(entityManager, times(1))
                .createNativeQuery(anyString(), eq(Order.class));
    }
}