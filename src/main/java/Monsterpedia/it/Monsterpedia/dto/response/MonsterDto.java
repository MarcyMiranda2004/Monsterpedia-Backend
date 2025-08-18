package Monsterpedia.it.Monsterpedia.dto.response;

import lombok.Data;

@Data
public class MonsterDto {
    private long id;
    private String name;
    private String category;
    private String flavor;
    private String origin;
    private String description;
    private String story;
    private String imageUrl;
    private String marcyopinion;
}
