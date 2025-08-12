package Monsterpedia.it.Monsterpedia.repository;

import Monsterpedia.it.Monsterpedia.model.FavoriteList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteListRepository extends JpaRepository <FavoriteList, Long> {
    Optional<FavoriteList> findByUserId(Long userId);
}
