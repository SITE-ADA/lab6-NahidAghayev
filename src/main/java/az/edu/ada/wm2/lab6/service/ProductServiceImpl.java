package az.edu.ada.wm2.lab6.service;

import az.edu.ada.wm2.lab6.model.Category;
import az.edu.ada.wm2.lab6.model.Product;
import az.edu.ada.wm2.lab6.model.dto.ProductRequestDto;
import az.edu.ada.wm2.lab6.model.dto.ProductResponseDto;
import az.edu.ada.wm2.lab6.model.mapper.ProductMapper;
import az.edu.ada.wm2.lab6.repository.CategoryRepository;
import az.edu.ada.wm2.lab6.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        validatePrice(dto.getPrice());

        Product product = productMapper.toEntity(dto);

        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());

            product.setCategories(categories);

            for (Category category : categories) {
                if (category.getProducts() == null) {
                    category.setProducts(new ArrayList<>());
                }
                category.getProducts().add(product);
            }

        } else {
            product.setCategories(new ArrayList<>());
        }

        Product saved = productRepository.save(product);
        return productMapper.toResponseDto(saved);
    }

    @Override
    public ProductResponseDto getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return productMapper.toResponseDto(product);
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    @Override
    public ProductResponseDto updateProduct(UUID id, ProductRequestDto dto) {
        validatePrice(dto.getPrice());

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existing.setProductName(dto.getProductName());
        existing.setPrice(dto.getPrice());
        existing.setExpirationDate(dto.getExpirationDate());

        Product saved = productRepository.save(existing);
        return productMapper.toResponseDto(saved);
    }

    @Override
    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    @Override
    public List<ProductResponseDto> getProductsExpiringBefore(LocalDate date) {
        return productRepository.findByExpirationDateBefore(date)
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }
}