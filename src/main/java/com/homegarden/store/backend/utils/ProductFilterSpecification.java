package com.homegarden.store.backend.utils;

import com.homegarden.store.backend.model.entity.Product;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductFilterSpecification {

    public static Specification<Product> filter(
            Long categoryId,
            Double minPrice,
            Double maxPrice,
            Boolean discount,
            String sort
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), categoryId));
            }

            if (minPrice != null) {
                predicates.add(criteriaBuilder.ge(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.le(root.get("price"), maxPrice));
            }

            if (discount != null && discount == true) {
                predicates.add(criteriaBuilder.isNotNull(root.get("discountPrice")));
            }

            if (sort != null) {
                Path<?> sortPath = root.get("price");
                if (sort.equalsIgnoreCase("ASC")) {
                    query.orderBy(criteriaBuilder.asc(sortPath));
                } else if (sort.equalsIgnoreCase("DESC")) {
                    query.orderBy(criteriaBuilder.desc(sortPath));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}