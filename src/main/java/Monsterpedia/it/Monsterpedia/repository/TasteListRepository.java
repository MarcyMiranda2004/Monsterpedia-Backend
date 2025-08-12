package Monsterpedia.it.Monsterpedia.repository;

import Monsterpedia.it.Monsterpedia.model.TasteList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TasteListRepository extends JpaRepository<TasteList, Long> {
    Optional<TasteList> findByUserId(Long userId);
}
