package Monsterpedia.it.Monsterpedia.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToFavoriteListRequestDto {
    @NotNull(message = "id monster non specificato")
    private Long monsterId;
}
