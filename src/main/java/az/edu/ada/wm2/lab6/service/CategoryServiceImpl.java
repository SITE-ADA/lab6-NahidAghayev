package az.edu.ada.wm2.lab6.service;

import az.edu.ada.wm2.lab6.model.Category;
import az.edu.ada.wm2.lab6.model.Product;
import az.edu.ada.wm2.lab6.model.dto.CategoryRequestDto;
import az.edu.ada.wm2.lab6.model.dto.CategoryResponseDto;
import az.edu.ada.wm2.lab6.model.dto.ProductResponseDto;
import az.edu.ada.wm2.lab6.model.mapper.CategoryMapper;
import az.edu.ada.wm2.lab6.model.mapper.ProductMapper;
import az.edu.ada.wm2.lab6.repository.CategoryRepository;
import az.edu.ada.wm2.lab6.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductMapper productMapper, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    @Override
    public CategoryResponseDto create(CategoryRequestDto dto) {
        Category category = CategoryMapper.toEntity(dto);

        Category savedCatagory = categoryRepository.save(category);

        return CategoryMapper.toResponseDto(savedCatagory);
    }

    @Override
    public List<CategoryResponseDto> getAll() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(CategoryMapper::toResponseDto).toList();
    }

    @Override
    public CategoryResponseDto addProduct(UUID categoryId, UUID productId) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (!existingCategory.getProducts().contains(product)) {
            existingCategory.getProducts().add(product);
        }

        if (!product.getCategories().contains(existingCategory)) {
            product.getCategories().add(existingCategory);
        }
        Category savedCategory = categoryRepository.save(existingCategory);

        return CategoryMapper.toResponseDto(savedCategory);
    }

    @Override
    public List<ProductResponseDto> getProducts(UUID categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        List<Product> products = existingCategory.getProducts();

        return products.stream().map(productMapper::toResponseDto).toList();
    }
}
