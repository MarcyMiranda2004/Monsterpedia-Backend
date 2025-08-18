package Monsterpedia.it.Monsterpedia.dto.response;

import lombok.Data;

@Data
public class FavoriteListItemDto {
    private Long monsterId;
    private String monsterName;
    private String imageUrl;
}
