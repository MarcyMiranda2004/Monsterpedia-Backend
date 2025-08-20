package Monsterpedia.it.Monsterpedia.dto.request;

import Monsterpedia.it.Monsterpedia.enumerated.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMonsterRequestDto {

    @NotBlank(message = "inserisci il nome della monster")
    private String name;

    @NotNull(message = "specificare la categoria della monster")
    private Category category;

    private String flavor;

    private String origin;

    private String description;

    private String story;

    private String imageUrl;

    private String marcyOpinion;
}
