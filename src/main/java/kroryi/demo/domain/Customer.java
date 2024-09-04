package kroryi.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="email")
    private String email;

    @Column(name="age")
    private int age;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name="address_id", referencedColumnName = "id")
//    private Address address;

    //CascadeType.REMOVE 부모 엔티티가 삭제될 때 자식 엔티티로 함께 삭제
    //orphanRemoveal = true 부모 엔티티가 자식 엔티티와의 관계를 끊을대
    // 자식 엔티티가 고아 상태가 될때 삭제
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;



}

