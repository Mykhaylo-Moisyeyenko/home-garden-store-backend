package com.homegarden.store.backend.utils;

import com.homegarden.store.backend.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductFilterSpecificationTest {

    @Mock
    private Root<Product> root;
    @Mock
    private CriteriaQuery<?> query;
    @Mock
    private CriteriaBuilder cb;
    @Mock
    private Predicate predicate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenAllParamsAreNull_thenReturnsAlwaysTruePredicate() {
        Specification<Product> spec = ProductFilterSpecification.filter(null, null, null, null, null);
        when(cb.and()).thenReturn(predicate);

        Predicate result = spec.toPredicate(root, query, cb);

        assertThat(result).isNotNull();
    }

    @Test
    void whenCategoryIdProvided_thenPredicateIncludesCategoryId() {
        Specification<Product> spec = ProductFilterSpecification.filter(10L, null, null, null, null);
        when(root.get("category")).thenReturn(null);
        when(cb.equal(any(), eq(10L))).thenReturn(predicate);
        when(cb.and(any(Predicate[].class))).thenReturn(predicate);

        Predicate result = spec.toPredicate(root, query, cb);

        assertThat(result).isNotNull();
    }

    @Test
    void whenMinPriceProvided_thenPredicateIncludesMinPrice() {
        Specification<Product> spec = ProductFilterSpecification.filter(null, new BigDecimal("100"), null, null, null);
        when(root.get("price")).thenReturn(null);
        when(cb.greaterThanOrEqualTo(any(), eq(new BigDecimal("100")))).thenReturn(predicate);
        when(cb.and(any(Predicate[].class))).thenReturn(predicate);

        Predicate result = spec.toPredicate(root, query, cb);

        assertThat(result).isNotNull();
    }

    @Test
    void whenMaxPriceProvided_thenPredicateIncludesMaxPrice() {
        Specification<Product> spec = ProductFilterSpecification.filter(null, null, new BigDecimal("200"), null, null);
        when(root.get("price")).thenReturn(null);
        when(cb.lessThanOrEqualTo(any(), eq(new BigDecimal("200")))).thenReturn(predicate);
        when(cb.and(any(Predicate[].class))).thenReturn(predicate);

        Predicate result = spec.toPredicate(root, query, cb);

        assertThat(result).isNotNull();
    }

    @Test
    void whenDiscountTrue_thenPredicateIncludesIsNotNullDiscountPrice() {
        Specification<Product> spec = ProductFilterSpecification.filter(null, null, null, true, null);
        when(root.get("discountPrice")).thenReturn(null);
        when(cb.isNotNull(any())).thenReturn(predicate);
        when(cb.and(any(Predicate[].class))).thenReturn(predicate);

        Predicate result = spec.toPredicate(root, query, cb);

        assertThat(result).isNotNull();
    }

    @Test
    void whenDiscountFalse_thenPredicateIncludesIsNullDiscountPrice() {
        Specification<Product> spec = ProductFilterSpecification.filter(null, null, null, false, null);
        when(root.get("discountPrice")).thenReturn(null);
        when(cb.isNull(any())).thenReturn(predicate);
        when(cb.and(any(Predicate[].class))).thenReturn(predicate);

        Predicate result = spec.toPredicate(root, query, cb);

        assertThat(result).isNotNull();
    }

    @Test
    void whenSortAsc_thenOrderByAscIsApplied() {
        Specification<Product> spec = ProductFilterSpecification.filter(null, null, null, null, "ASC");
        when(root.get("price")).thenReturn(null);
        when(cb.asc(any())).thenReturn(mock(jakarta.persistence.criteria.Order.class));
        when(query.orderBy(anyList())).thenReturn((CriteriaQuery) query);
        when(cb.and(any(Predicate[].class))).thenReturn(predicate);

        Predicate result = spec.toPredicate(root, query, cb);

        assertThat(result).isNotNull();
    }

    @Test
    void whenSortDesc_thenOrderByDescIsApplied() {
        Specification<Product> spec = ProductFilterSpecification.filter(null, null, null, null, "DESC");
        when(root.get("price")).thenReturn(null);
        when(cb.desc(any())).thenReturn(mock(jakarta.persistence.criteria.Order.class));
        when(query.orderBy(anyList())).thenReturn((CriteriaQuery) query);
        when(cb.and(any(Predicate[].class))).thenReturn(predicate);

        Predicate result = spec.toPredicate(root, query, cb);

        assertThat(result).isNotNull();
    }
}