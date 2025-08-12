package Monsterpedia.it.Monsterpedia.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "favorite_lists")
public class TasteList {
    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "tasteList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TasteListItem> items = new ArrayList<>();
}
