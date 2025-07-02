package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.config.SecurityConfig;
import com.homegarden.store.backend.model.dto.CategoryDto;
import com.homegarden.store.backend.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@Import(SecurityConfig.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryServiceTest;

    @Test
    void createTest() {

    }

    @Test
    void getAllTest() {
    }

    @Test
    void getByIdTest() throws Exception {
        when(categoryServiceTest.getById(anyLong())).thenReturn(
                new CategoryDto(2L, "tools")
        );

        mockMvc.perform(get("/v1/categories/{id}", 2L))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").exists())
                .andExpect(jsonPath("$.categoryId").value(2L))
                .andExpect(jsonPath("$.name").value("tools"));
    }

    @Test
    void deleteTest() {
    }

    @Test
    void updateCategoryTest() {
    }
}