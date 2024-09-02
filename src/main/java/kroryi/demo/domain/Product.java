package kroryi.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
//StackOverflower:null 이런 에러 발생 시
// 아래와 같이 ToString에 순환참조 되는 것을 제외 시켜야 함.
@ToString(exclude = "category")
@Table(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;
    private int quantity;

    @ManyToOne
    @JoinColumn(name="category_id", nullable = false)
    private Category category;


}
