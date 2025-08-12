package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.ProfitReportDto;
import com.homegarden.store.backend.dto.TopCancelledProductDto;
import com.homegarden.store.backend.exception.ReportBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{

    private final OrderItemService orderItemService;
    private final OrderService orderService;

    @Override
    public List<TopCancelledProductDto> getTopCancelledProducts() {
        List<Object[]> data = orderItemService.getTopCancelledProducts();

        return data.stream()
                .map(obj -> new TopCancelledProductDto(
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

        List<Object[]> rowsFromDb =
                switch (groupBy) {
                    case "HOUR" -> orderService.getGroupedRevenue(startTime, endTime, "hour");
                    case "DAY" -> orderService.getGroupedRevenue(startTime, endTime, "day");
                    case "WEEK" -> orderService.getGroupedRevenue(startTime, endTime, "week");
                    case "MONTH" -> orderService.getGroupedRevenue(startTime, endTime, "month");
                    default -> throw new ReportBadRequestException("Wrong groupBy parameter");
                };

        return rowsFromDb.stream()
                .map(row -> new ProfitReportDto(
                        ((Timestamp) row[0]).toLocalDateTime(),
                        (BigDecimal) row[1]
                ))
                .toList();
    }
}