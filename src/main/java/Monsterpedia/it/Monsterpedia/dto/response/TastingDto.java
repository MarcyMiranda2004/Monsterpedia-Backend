package Monsterpedia.it.Monsterpedia.dto.response;

import Monsterpedia.it.Monsterpedia.enumerated.Tier;

public record TastingDto(
        Long id,
        Long monsterId,
        String monsterName,
        String monsterImageUrl,
        int rating,
        Tier tier,
        String comment
) {}
