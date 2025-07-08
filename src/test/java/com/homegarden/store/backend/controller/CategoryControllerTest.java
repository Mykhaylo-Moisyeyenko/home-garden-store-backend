package com.homegarden.store.backend.controller;

import com.homegarden.store.backend.converter.CategoryConverter;
import com.homegarden.store.backend.model.dto.CategoryDto;
import com.homegarden.store.backend.model.entity.Category;
import com.homegarden.store.backend.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryServiceTest;

    @MockitoBean
    private CategoryConverter categoryConverterTest;

    Category category1 = new Category(1L, "tools", null);

    Category category2 = new Category(2L, "seeds", null);

    List<Category> categoryList = List.of(category1, category2);

    CategoryDto categoryDto1 = new CategoryDto(1L, "tools");

    CategoryDto categoryDto2 = new CategoryDto(2L, "seeds");

    List<CategoryDto> categoryDtoList = List.of(categoryDto1, categoryDto2);

    @Test
    void createTest() throws Exception {
        when(categoryServiceTest.create(category1)).thenReturn(category1);
        when(categoryConverterTest.toDto(category1)).thenReturn(categoryDto1);
        when(categoryConverterTest.toEntity(categoryDto1)).thenReturn(category1);

        mockMvc.perform(
                        post("/v1/categories")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content("{\"categoryId\":1,\"name\":\"tools\"}")
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId").exists())
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.name").value("tools"));
    }

    @Test
    void getAllTest() throws Exception {
        when(categoryServiceTest.getAll()).thenReturn(categoryList);
        when(categoryConverterTest.toDto(categoryList.get(0))).thenReturn(categoryDtoList.get(0));
        when(categoryConverterTest.toDto(categoryList.get(1))).thenReturn(categoryDtoList.get(1));

        mockMvc.perform(get("/v1/categories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].categoryId").exists())
                .andExpect(jsonPath("$[0].categoryId").value(1L))
                .andExpect(jsonPath("$[0].name").value("tools"))
                .andExpect(jsonPath("$[1].categoryId").exists())
                .andExpect(jsonPath("$[1].categoryId").value(2L))
                .andExpect(jsonPath("$[1].name").value("seeds"));
    }

    @Test
    void getByIdTest() throws Exception {
        when(categoryServiceTest.getById(category1.getCategoryId())).thenReturn(category1);
        when(categoryConverterTest.toDto(category1)).thenReturn(categoryDto1);

        mockMvc.perform(get("/v1/categories/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").exists())
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.name").value("tools"));
    }

    @Test
    void deleteTest() throws Exception {
        Mockito.doNothing().when(categoryServiceTest).delete(category1.getCategoryId());

        mockMvc.perform(delete("/v1/categories/{id}",1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateCategoryTest() throws Exception {
        Category updatedCategory = new Category(1L, "flowers", null);
        CategoryDto updatedCategoryDto = new CategoryDto(1L,"flowers");

        when(categoryServiceTest.update(category1.getCategoryId(),"flowers")).thenReturn(updatedCategory);
        when(categoryConverterTest.toDto(updatedCategory)).thenReturn(updatedCategoryDto);

        mockMvc.perform(put("/v1/categories/{id}",1L).contentType(APPLICATION_JSON_UTF8)
                .content("{\"categoryId\":1,\"name\":\"flowers\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").exists())
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.name").value("flowers"));

    }
}