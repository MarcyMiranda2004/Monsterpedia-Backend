package Monsterpedia.it.Monsterpedia.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteUserDto {
    @NotBlank(message = "Devi inserire la password per confermare")
    private String password;
}
