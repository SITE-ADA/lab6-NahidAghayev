package az.edu.ada.wm2.lab6.model.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private UUID id;
    private String name;
}
