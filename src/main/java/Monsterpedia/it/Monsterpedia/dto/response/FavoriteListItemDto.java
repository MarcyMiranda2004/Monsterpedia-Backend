package Monsterpedia.it.Monsterpedia.dto.response;

import Monsterpedia.it.Monsterpedia.enumerated.Tier;
import lombok.Data;

@Data
public class FavoriteListItemDto {
    private Long monsterId;
    private String monsterName;
    private String imageUrl;
    private int rating;
    private Tier tier;
    private String comment;
}
