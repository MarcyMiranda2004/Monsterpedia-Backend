package Monsterpedia.it.Monsterpedia.controller;

import Monsterpedia.it.Monsterpedia.dto.request.CreateMonsterRequestDto;
import Monsterpedia.it.Monsterpedia.dto.response.MonsterDto;
import Monsterpedia.it.Monsterpedia.exception.NotFoundException;
import Monsterpedia.it.Monsterpedia.model.Monster;
import Monsterpedia.it.Monsterpedia.service.MonsterService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monsters")
public class MonsterController {
    @Autowired MonsterService monsterService;

    @GetMapping("/search")
    public ResponseEntity<Page<MonsterDto>> search(
            @RequestParam(required = false) String name,
            @PageableDefault(page = 0, size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(monsterService.search(name, pageable));
    }

    @GetMapping
    public ResponseEntity<List<Monster>> getAllMonster() {
        return ResponseEntity.ok(monsterService.getAllMonster());
    }

    @GetMapping("/category/{category}")
    public List<Monster> getMonstersByCategory(@PathVariable String category) {
        return monsterService.getAllByCategory(category);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<MonsterDto> getById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(monsterService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MonsterDto> create(@RequestBody @Valid CreateMonsterRequestDto cmrd, BindingResult br) {
        if (br.hasErrors()) {
            String errs = br.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .reduce((a, b) -> a + "; " + b).orElse("");
            throw new ValidationException(errs);
        }
        MonsterDto created = monsterService.create(cmrd);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MonsterDto> update(
            @PathVariable Long id,
            @RequestBody @Valid CreateMonsterRequestDto cmrd,
            BindingResult br
    ) {
        if (br.hasErrors()) {
            String errs = br.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .reduce((a, b) -> a + "; " + b).orElse("");
            throw new ValidationException(errs);
        }
        MonsterDto updated = monsterService.update(id, cmrd);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        monsterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
