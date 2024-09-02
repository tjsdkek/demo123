package kroryi.demo.repository;

import kroryi.demo.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByTotalAmountGreaterThan(BigDecimal totalAmount);

    List<Order> findByOrOrderDateAfter(LocalDateTime date);

    List<Order> findByOrOrderDateBefore(LocalDateTime date);

    List<Order> findByOrderDateBetween(LocalDateTime date1, LocalDateTime date2);

}
