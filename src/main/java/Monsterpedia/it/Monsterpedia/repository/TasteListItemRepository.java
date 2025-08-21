package Monsterpedia.it.Monsterpedia.repository;

import Monsterpedia.it.Monsterpedia.model.TasteListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TasteListItemRepository extends JpaRepository<TasteListItem, Long> {
    Optional<TasteListItem> findByTasteListUserIdAndMonsterId(Long userId, Long monsterId);
}
