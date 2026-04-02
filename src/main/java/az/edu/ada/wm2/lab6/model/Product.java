package az.edu.ada.wm2.lab6.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String productName;
    private BigDecimal price;
    private LocalDate expirationDate;

    @ManyToMany
    @Builder.Default
    @JoinTable(name = "product_categories",
                joinColumns = @JoinColumn(name = "product_id"),
                inverseJoinColumns = @JoinColumn(name = "category_id")
                )
    private List<Category> categories = new ArrayList<>();
}
