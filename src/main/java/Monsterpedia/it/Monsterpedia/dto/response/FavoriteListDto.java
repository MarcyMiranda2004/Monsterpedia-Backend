package Monsterpedia.it.Monsterpedia.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class FavoriteListDto {
    private Long favoriteListId;
    private List<FavoriteListItemDto> items;
}
