package Monsterpedia.it.Monsterpedia.dto.response;

import lombok.Data;

@Data
public class TastedListItemDto {
    private Long monsterId;
    private String monsterName;
    private String imageUrl;
    private int rating;
    private String comment;
}
