package com.homegarden.store.backend.service;

import com.homegarden.store.backend.model.entity.Category;
import com.homegarden.store.backend.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    Category category1 = new Category(1L, "tools");

    @Test
    void createTest() throws Exception {
        when(categoryRepository.save(category1)).thenReturn(category1);
        Category actual = categoryService.create(category1);
        assertEquals(category1, actual);
    }

    @Test
    void getAllTest() throws Exception {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1));
        List<Category> actual = categoryService.getAll();
        assertEquals(Arrays.asList(category1), actual);
    }

    @Test
    void getByIdTest() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        Category actual = categoryService.getById(1L);
        assertEquals(category1, actual);
    }

    @Test
    void updateTest() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1))
                .thenReturn(Optional.of(category1));
        Category category2 = new Category(1L, "trees");
        when(categoryRepository.save(category2)).thenReturn(category2);
        Category actual = categoryService.update(1L, category2.getName());
        assertEquals(category2, actual);

    }

    @Test
    void deleteTest() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        categoryService.delete(1L);
    }
}
