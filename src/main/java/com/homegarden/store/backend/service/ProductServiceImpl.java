package com.homegarden.store.backend.service;

import com.homegarden.store.backend.exception.ProductNotFoundException;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.exception.ProductUsedInOrdersException;
import com.homegarden.store.backend.repository.ProductRepository;
import com.homegarden.store.backend.utils.ProductFilterSpecification;
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
            String sort
    ) {
        Specification<Product> specification = ProductFilterSpecification.filter(
                categoryId, minPrice, maxPrice, discount, sort);
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
        Long id = product.getProductId();

        Product existing = getById(id);

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
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
}