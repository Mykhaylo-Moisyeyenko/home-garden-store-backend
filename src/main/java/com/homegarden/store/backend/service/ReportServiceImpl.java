package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.TopCancelledProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{

    private final OrderItemService orderItemService;

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
}