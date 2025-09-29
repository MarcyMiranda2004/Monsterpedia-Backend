package Monsterpedia.it.Monsterpedia.repository;

import Monsterpedia.it.Monsterpedia.model.Monster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonsterRepository extends JpaRepository<Monster, Long> {
    Page<Monster> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    List<Monster> findAllByCategory(String category);
}
