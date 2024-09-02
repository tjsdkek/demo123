package kroryi.demo.repository;

import kroryi.demo.domain.Category;
import kroryi.demo.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //명명규칙을 따른다. Named Rule
    List<Product> findByCategory(Category category);
    List<Product> findByPriceGreaterThan(BigDecimal price);
    List<Product> findByCategory_Name(String categoryName);
}
