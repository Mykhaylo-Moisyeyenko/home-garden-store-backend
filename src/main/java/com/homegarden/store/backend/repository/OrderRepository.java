package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.entity.Order;
import com.homegarden.store.backend.entity.Product;
import com.homegarden.store.backend.entity.User;
import com.homegarden.store.backend.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser(User user);

    List<Order> findByStatusAndUpdatedAtAfter(Status status, LocalDateTime updatedAtAfter);

    List<Order> findByStatusAndUpdatedAtBefore(Status status, LocalDateTime updatedAtBefore);

    @Query(nativeQuery = true, value = """
            SELECT
                CASE
                    WHEN :timeCut = 'hour'  THEN date_trunc('hour', o.updated_at)
                    WHEN :timeCut = 'day'   THEN date_trunc('day', o.updated_at)
                    WHEN :timeCut = 'week'  THEN date_trunc('week', o.updated_at)
                    WHEN :timeCut = 'month' THEN date_trunc('month', o.updated_at)
                END AS period,
                SUM(o.order_total_sum) AS revenue
            FROM orders AS o
            WHERE o.status = 'DELIVERED'
                AND o.updated_at >= :startPeriod
                AND o.updated_at < :endPeriod
            GROUP BY period
            ORDER BY period
            """)
    List<Object[]> findGroupedRevenue(@Param("startPeriod") LocalDateTime startTime,
                                      @Param("endPeriod") LocalDateTime endTime,
                                      @Param("timeCut") String timeCut);

    Optional<Order> findByOrderIdAndUser(Long orderId, User user);
}