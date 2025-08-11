package com.homegarden.store.backend.service;

import com.homegarden.store.backend.dto.TopCancelledProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    void getTopCancelledProductsTest() {
            Object[] data = new Object[]{1L, "Product Name", 5L};
            when(orderItemService.getTopCancelledProducts()).thenReturn(List.<Object[]>of(data));

            List<TopCancelledProductDto> result = reportService.getTopCancelledProducts();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).productId()).isEqualTo(1L);
    }
}