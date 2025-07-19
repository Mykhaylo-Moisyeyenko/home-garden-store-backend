package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query(nativeQuery = true, value = """
            select
                oi.product_id as product_id,
                p.name as productName,
                count(distinct oi.order_id) as cancelCount
            from order_items as oi
                    join orders as o
                    on oi.order_id = o.order_id
            join products as p
                    on oi.product_id = p.product_id
            where o.status = 'CANCELLED'
                    group by oi.product_id, p.name
                            order by count(distinct oi.order_id) desc 
                                    limit 10 
            """)
    List<Object[]> findTopCancelledProducts();
}