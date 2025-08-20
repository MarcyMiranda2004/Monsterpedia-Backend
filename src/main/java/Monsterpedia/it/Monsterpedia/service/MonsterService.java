package Monsterpedia.it.Monsterpedia.service;

import Monsterpedia.it.Monsterpedia.dto.request.CreateMonsterRequestDto;
import Monsterpedia.it.Monsterpedia.dto.response.MonsterDto;
import Monsterpedia.it.Monsterpedia.exception.NotFoundException;
import Monsterpedia.it.Monsterpedia.model.Monster;
import Monsterpedia.it.Monsterpedia.repository.MonsterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MonsterService {

    @Autowired private MonsterRepository monsterRepository;

    public Page<MonsterDto> search(String name, Pageable pageable) {
        return monsterRepository.findByNameContainingIgnoreCase(name == null ? "" : name, pageable).map(this::toDto);
    }

    public MonsterDto getById(Long id) throws NotFoundException {
        Monster m = monsterRepository.findById(id).orElseThrow(() -> new NotFoundException("Monster non trovata"));
        return toDto(m);
    }

    public Monster getMonsterById(Long id) throws NotFoundException {
        return monsterRepository.findById(id).orElseThrow(() -> new NotFoundException("Monster non trovata"));
    }

    @Transactional
    public MonsterDto create(CreateMonsterRequestDto requestDto) {
        Monster m =  new Monster();
        m.setName(requestDto.getName());
        m.setCategory(requestDto.getCategory());
        m.setFlavor(requestDto.getFlavor());
        m.setOrigin(requestDto.getOrigin());
        m.setDescription(requestDto.getDescription());
        m.setImageUrl(requestDto.getImageUrl());
        m.setMarcyOpinion(requestDto.getMarcyOpinion());;
        Monster newMonster = monsterRepository.save(m);
        return toDto(newMonster);
    }

    @Transactional
    public MonsterDto update(Long id, CreateMonsterRequestDto requestDto) throws NotFoundException {
        Monster m = monsterRepository.findById(id).orElseThrow(() -> new NotFoundException("Monster non trovata"));
        m.setName(requestDto.getName());
        m.setCategory(requestDto.getCategory());
        m.setFlavor(requestDto.getFlavor());
        m.setOrigin(requestDto.getOrigin());
        m.setDescription(requestDto.getDescription());
        m.setImageUrl(requestDto.getImageUrl());
        m.setMarcyOpinion(requestDto.getMarcyOpinion());
        Monster updatedMonster = monsterRepository.save(m);
        return toDto(updatedMonster);
    }

    @Transactional
    public void delete(Long id) throws NotFoundException {
        Monster m = monsterRepository.findById(id).orElseThrow(() -> new NotFoundException("Monster non trovata"));
        monsterRepository.delete(m);
    }

    private MonsterDto toDto(Monster m) {
        MonsterDto monsterDto = new MonsterDto();
        monsterDto.setId(monsterDto.getId());
        monsterDto.setName(monsterDto.getName());
        monsterDto.setCategory(monsterDto.getCategory());
        monsterDto.setFlavor(monsterDto.getFlavor());
        monsterDto.setOrigin(monsterDto.getOrigin());
        monsterDto.setDescription(monsterDto.getDescription());
        monsterDto.setImageUrl(monsterDto.getImageUrl());
        monsterDto.setMarcyopinion(monsterDto.getMarcyopinion());
        return monsterDto;
    }
}
