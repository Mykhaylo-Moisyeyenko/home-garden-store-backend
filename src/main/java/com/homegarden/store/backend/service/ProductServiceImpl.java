package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.exception.ProductNotFoundException;
import com.homegarden.store.backend.exception.ProductUsedInOrdersException;
import com.homegarden.store.backend.repository.ProductRepository;
import com.homegarden.store.backend.utils.ProductFilterSpecification;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final EntityManager entityManager;

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAll(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean discount,
            String sort) {

        Specification<Product> specification = ProductFilterSpecification.filter(
                categoryId,
                minPrice,
                maxPrice,
                discount,
                sort);

        return productRepository.findAll(specification);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Override
    public void delete(Long id) {
        getById(id);
        if (orderService.isProductUsedInOrders(id)) {
            throw new ProductUsedInOrdersException("Unable to delete product with id: " + id + " because it is used in Orders");
        }
        productRepository.deleteById(id);
    }

    @Override
    public Product update(Product product) {
        Product existing = getById(product.getProductId());
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());

        if (product.getCategory() != null) {
            existing.setCategory(product.getCategory());
        }

        existing.setImageUrl(product.getImageUrl());
        existing.setDiscountPrice(product.getDiscountPrice());
        existing.setUpdatedAt(product.getUpdatedAt());

        return productRepository.save(existing);
    }

    @Override
    public Product setDiscountPrice(Long productId, BigDecimal newDiscountPrice) {
        Product product = getById(productId);
        product.setDiscountPrice(newDiscountPrice);

        return productRepository.save(product);
    }

    @Override
    public Product getProductOfTheDay() {
        String query = """
                WITH discount_data AS (SELECT p.product_id,
                                              (1 - p.discount_price / p.price) AS discountRate
                                       FROM products AS p
                                       WHERE p.discount_price IS NOT NULL),
                    max_discount AS (SELECT MAX (discountRate) AS maxRate
                                     FROM discount_data)
                SELECT p.*
                FROM products AS p
                JOIN discount_data AS d
                    ON p.product_id = d.product_id
                WHERE d.discountRate = (SELECT maxRate FROM max_discount)
                ORDER BY random()
                LIMIT 1;
                """;
        return (Product) entityManager.createNativeQuery(query, Product.class).getSingleResult();
    }
}