package Monsterpedia.it.Monsterpedia.model;

import Monsterpedia.it.Monsterpedia.enumerated.Tier;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "taste_list_items")
public class TasteListItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "taste_list_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private TasteList tasteList;

    @ManyToOne
    @JoinColumn(name = "monster_id", nullable = false)
    private Monster monster;

    @Min(0) @Max(10)
    @Column(nullable = false)
    private int rating;

    @Size(max = 2000)
    @Column(length = 2000)
    private String comment;

    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    @PrePersist void onCreate(){ createdAt = updatedAt = Instant.now(); }
    @PreUpdate  void onUpdate(){ updatedAt = Instant.now(); }

    @Transient
    public Tier getTier(){ return Tier.fromRating(rating); }
}
