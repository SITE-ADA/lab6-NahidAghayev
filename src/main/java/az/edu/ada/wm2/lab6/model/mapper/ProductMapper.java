package az.edu.ada.wm2.lab6.model.mapper;

import az.edu.ada.wm2.lab6.model.Category;
import az.edu.ada.wm2.lab6.model.Product;
import az.edu.ada.wm2.lab6.model.dto.ProductRequestDto;
import az.edu.ada.wm2.lab6.model.dto.ProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // Entity → Response DTO
    @Mapping(target = "categoryNames", expression = "java(mapCategoriesToNames(product.getCategories()))")
    ProductResponseDto toResponseDto(Product product);

    // Request DTO → Entity (without categories)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Product toEntity(ProductRequestDto dto);

    // Null-safe conversion: List<Category> → List<String>
    default List<String> mapCategoriesToNames(List<Category> categories) {
        if (categories == null) return new ArrayList<>();
        return categories.stream()
                .map(Category::getName)
                .toList();
    }
}