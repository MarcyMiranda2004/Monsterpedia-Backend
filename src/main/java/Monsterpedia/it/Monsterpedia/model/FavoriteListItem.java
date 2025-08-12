package Monsterpedia.it.Monsterpedia.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "favorite_list_items")
public class FavoriteListItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "favorite_list_id", nullable = false)
    private FavoriteList favoriteList;

    @ManyToOne
    @JoinColumn(name = "monster_id", nullable = false)
    private Monster monster;
}
