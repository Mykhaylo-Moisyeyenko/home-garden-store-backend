package com.homegarden.store.backend.repository.specification;

import com.homegarden.store.backend.model.entity.Product;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProductFilterSpecification {

    public static Specification<Product> filter(
            Long categoryId,
            Double minPrice,
            Double maxPrice,
            Boolean discount,
            String sort
    ) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (categoryId != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("categoryId"), categoryId));
            }

            if (minPrice != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.ge(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.le(root.get("price"), maxPrice));
            }

            if (discount != null && discount == true) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.isNotNull(root.get("discount")));
            }

            if (sort != null) {
                Path<?> sortPath = root.get("price");
                if (sort.equalsIgnoreCase("ASC")) {
                    query.orderBy(criteriaBuilder.asc(sortPath));
                } else if (sort.equalsIgnoreCase("DESC")) {
                    query.orderBy(criteriaBuilder.desc(sortPath));
                }
            }
            return predicate;
        };
    }
}