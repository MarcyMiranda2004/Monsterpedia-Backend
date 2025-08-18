package Monsterpedia.it.Monsterpedia.service;

import Monsterpedia.it.Monsterpedia.dto.response.TastedListDto;
import Monsterpedia.it.Monsterpedia.dto.response.TastedListItemDto;
import Monsterpedia.it.Monsterpedia.exception.NotFoundException;
import Monsterpedia.it.Monsterpedia.model.*;
import Monsterpedia.it.Monsterpedia.repository.MonsterRepository;
import Monsterpedia.it.Monsterpedia.repository.TasteListRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TastedLIstService {

    @Autowired UserService userService;
    @Autowired TasteListRepository tasteListRepository;
    @Autowired MonsterRepository monsterRepository;

    public TastedListDto viewTastedList(Long userId) throws NotFoundException {
        TasteList tasteList = tasteListRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("TasteList non trovata per user " + userId));
        TastedListDto tastedListDto = new  TastedListDto();
        tastedListDto.setTastedListId(tastedListDto.getTastedListId());
        tastedListDto.setItems(tastedListDto.getItems());
        return tastedListDto;
    }

    public List<TasteListItem> getTastedListItems(long userId) throws NotFoundException {
        TasteList tasteList = tasteListRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("TasteList non trovata per user " + userId));
        return tasteList.getItems();
    }

    private TastedListItemDto toDto(FavoriteListItem i) {
        TastedListItemDto tlid = new TastedListItemDto();
        tlid.setMonsterId(tlid.getMonsterId());
        tlid.setMonsterName(tlid.getMonsterName());
        tlid.setImageUrl(tlid.getImageUrl());
        return tlid;
    }

    @Transactional
    public TastedListDto addToTastedList(long userId, long monsterId) throws NotFoundException {
        User u = userService.getUser(userId);
        Monster m = monsterRepository.findById(monsterId)
                .orElseThrow(() -> new NotFoundException("Monster non trovato: " + monsterId));
        TasteList tastedList = tasteListRepository.findByUserId(userId)
                .orElseGet(() -> {
                    TasteList tl = new TasteList();
                    tl.setUser(tl.getUser());
                    return tl;
                });
        Optional<TasteListItem> existing = tastedList.getItems().stream()
                .filter(item -> item.getMonster().getId().equals(monsterId)).findAny();
        TasteListItem item = existing.orElseGet(() -> {
            TasteListItem tli = new TasteListItem();
            tli.setTasteList(tastedList);
            tli.setMonster(m);
            tastedList.getItems().add(tli);
            return tli;
        });

        tasteListRepository.save(tastedList);
        return viewTastedList(userId);
    }

    @Transactional
    public TastedListDto removeItemFromTastedList(long userId, long monsterId) throws NotFoundException {
        TasteList tasteList = tasteListRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("TasteList non trovata per user " + userId));
        tasteList.getItems().removeIf(i -> i.getMonster().getId().equals(monsterId));
        tasteListRepository.save(tasteList);
        return viewTastedList(userId);
    }
}
