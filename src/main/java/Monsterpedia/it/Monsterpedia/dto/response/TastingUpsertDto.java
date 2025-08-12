package Monsterpedia.it.Monsterpedia.dto.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TastingUpsertDto(
        @NotNull Long monsterId,
        @Min(0) @Max(5) int rating,
        @Size(max = 2000) String comment
) {}
