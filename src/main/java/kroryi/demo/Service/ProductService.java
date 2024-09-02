package kroryi.demo.Service;

import kroryi.demo.domain.Category;
import kroryi.demo.domain.Product;
import kroryi.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {

        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(Category category) {

        return productRepository.findByCategory(category);
    }

    public List<Product> getCategory_Name(String name) {

        return productRepository.findByCategory_Name(name);
    }

    public List<Product> getProductsPriceGreaterThan(BigDecimal price) {

        return productRepository.findByPriceGreaterThan(price);
    }
}
