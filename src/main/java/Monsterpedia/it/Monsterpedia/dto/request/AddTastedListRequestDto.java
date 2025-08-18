package Monsterpedia.it.Monsterpedia.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddTastedListRequestDto {
    @NotNull(message = "id monster non specificato")
    private long monsterId;
}
