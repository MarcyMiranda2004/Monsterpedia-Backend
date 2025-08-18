package Monsterpedia.it.Monsterpedia.dto.response;

import Monsterpedia.it.Monsterpedia.model.TasteListItem;
import lombok.Data;

import java.util.List;

@Data
public class TastedListDto {
    private Long tastedListId;
    private List<TasteListItem> items;
}


