package Monsterpedia.it.Monsterpedia.model;

import Monsterpedia.it.Monsterpedia.enumerated.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "monsters")
public class Monster {
    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String flavor;

    private String origin;

    private String description;

    private String story;

    private String imageUrl;

    private String marcyOpinion;
}
