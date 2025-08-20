package Monsterpedia.it.Monsterpedia.dto.response;

import Monsterpedia.it.Monsterpedia.enumerated.Category;
import lombok.Data;

@Data
public class MonsterDto {
    private long id;
    private String name;
    private Category category;
    private String flavor;
    private String origin;
    private String description;
    private String story;
    private String imageUrl;
    private String marcyOpinion;
}
