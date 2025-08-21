package Monsterpedia.it.Monsterpedia.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class TastedListDto {
    private Long tastedListId;
    private List<TastedListItemDto> items;
}


