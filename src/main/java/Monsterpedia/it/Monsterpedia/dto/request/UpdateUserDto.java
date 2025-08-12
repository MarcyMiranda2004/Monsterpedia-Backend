package Monsterpedia.it.Monsterpedia.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserDto {
   @NotBlank(message = "lo username non può essere vuoto")
    private String username;
}
