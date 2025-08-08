package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query(nativeQuery = true, value = """
            
            SELECT
                oi.product_id AS product_id,
                p.name AS productName,
                COUNT(distinct oi.order_id) AS cancelCount
            FROM order_items AS oi
                    JOIN orders AS o
                    ON oi.order_id = o.order_id
            JOIN products AS p
                    ON oi.product_id = p.product_id
            WHERE o.status = 'CANCELLED'
                    GROUP BY oi.product_id, p.name
                            ORDER BY COUNT(DISTINCT oi.order_id) DESC
                                    LIMIT 10
            """)

    List<Object[]> findTopCancelledProducts();

    boolean existsByProductProductId(Long productId);
}