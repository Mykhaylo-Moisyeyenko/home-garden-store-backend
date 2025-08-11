package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.dto.TopCancelledProductDto;
import com.homegarden.store.backend.service.ReportService;
import com.homegarden.store.backend.service.security.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(ReportController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportService reportService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Test
    void getTopCancelledProductsTest() throws Exception {
        TopCancelledProductDto dto = new TopCancelledProductDto(10L, "Product Name", 5L);
        when(reportService.getTopCancelledProducts()).thenReturn(List.of(dto));

        mockMvc.perform(get("/v1/reports/top-cancelled-products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productId", is(10)))
                .andExpect(jsonPath("$[0].productName", is("Product Name")))
                .andExpect(jsonPath("$[0].cancelCount", is(5)));
    }
}