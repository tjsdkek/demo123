package kroryi.demo.Service;

import kroryi.demo.domain.Customer;
import kroryi.demo.domain.Order;
import kroryi.demo.domain.OrderItem;
import kroryi.demo.domain.Product;
import kroryi.demo.dto.OrderItemDTO;
import kroryi.demo.repository.CustomerRepository;
import kroryi.demo.repository.OrderRepository;
import kroryi.demo.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Iterable<Order> findAll() {

        return orderRepository.findAll();
    }

    public Optional<Order> findOne(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getCustomerId(Long customerId) {

        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> getTotalAmountGreaterThan(BigDecimal totalAmount) {
        return orderRepository.findByTotalAmountGreaterThan(totalAmount);
    }

    public List<Order> getOrderDateAfter(LocalDateTime localDateTime) {

        return orderRepository.findByOrOrderDateAfter(localDateTime);
    }

    public List<Order> getOrderDateBefore(LocalDateTime localDateTime) {
        return orderRepository.findByOrOrderDateBefore(localDateTime);
    }

    public List<Order> getOrderDateBetween(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {

        return orderRepository.findByOrderDateBetween(localDateTime1, localDateTime2);
    }


    //추후에 Order은 OrderDTO 바꿔야 함.
    @Transactional
    public Order createOrder(Long customerId, List<OrderItemDTO> orderItems) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                ()-> new RuntimeException("고객 없음.")
        );
        Order order = Order.builder()
                .customer(customer)
                .totalAmount(BigDecimal.ZERO)
                .orderItems(new ArrayList<>())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for(OrderItemDTO item : orderItems){
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if(product.getQuantity() < item.getQuantity()){
                throw new RuntimeException("재고가 없습니다.");
            }

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(item.getQuantity())
                    .price(product.getPrice())
                    .build();

            order.getOrderItems().add(orderItem);
            totalAmount = totalAmount.add(orderItem.getPrice());
            product.setQuantity(product.getQuantity() - item.getQuantity());

        }
        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }


}
