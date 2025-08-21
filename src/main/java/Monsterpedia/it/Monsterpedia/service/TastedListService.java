package Monsterpedia.it.Monsterpedia.service;

import Monsterpedia.it.Monsterpedia.dto.response.TastedListDto;
import Monsterpedia.it.Monsterpedia.dto.response.TastedListItemDto;
import Monsterpedia.it.Monsterpedia.exception.NotFoundException;
import Monsterpedia.it.Monsterpedia.model.*;
import Monsterpedia.it.Monsterpedia.repository.MonsterRepository;
import Monsterpedia.it.Monsterpedia.repository.TasteListItemRepository;
import Monsterpedia.it.Monsterpedia.repository.TasteListRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TastedListService {

    @Autowired UserService userService;
    @Autowired TasteListRepository tasteListRepository;
    @Autowired TasteListItemRepository tasteListItemRepository;
    @Autowired MonsterRepository monsterRepository;

    public TastedListDto viewTastedList(Long userId) throws NotFoundException {
        TasteList tasteList = tasteListRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("TasteList non trovata per user " + userId));
        TastedListDto tastedListDto = new  TastedListDto();
        tastedListDto.setTastedListId(tasteList.getId());
        tastedListDto.setItems(tasteList.getItems().stream().map(this::toDto).toList());
        return tastedListDto;
    }

    public List<TasteListItem> getTastedListItems(long userId) throws NotFoundException {
        TasteList tasteList = tasteListRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("TasteList non trovata per user " + userId));
        return tasteList.getItems();
    }

    private TastedListItemDto toDto(TasteListItem i) {
        TastedListItemDto tlid = new TastedListItemDto();
        tlid.setMonsterId(i.getMonster().getId());
        tlid.setMonsterName(i.getMonster().getName());
        tlid.setImageUrl(i.getMonster().getImageUrl());
        tlid.setRating(i.getRating());
        tlid.setComment(i.getComment());
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
                    tl.setUser(u);
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
    public TasteListItem setRating(Long userId, Long monsterId, int rating) throws NotFoundException {
        TasteList tasteList = getTasteListByUserId(userId);
        Monster monster = monsterRepository.findById(monsterId)
                .orElseThrow(() -> new NotFoundException("Monster non trovato"));

        TasteListItem item = tasteList.getItems().stream()
                .filter(i -> i.getMonster().getId().equals(monsterId))
                .findFirst()
                .orElseGet(() -> {
                    TasteListItem newItem = new TasteListItem();
                    newItem.setTasteList(tasteList);
                    newItem.setMonster(monster);
                    tasteList.getItems().add(newItem);
                    return newItem;
                });

        item.setRating(rating);
        tasteListItemRepository.save(item);
        return item;
    }

    @Transactional
    public TasteListItem setComment(Long userId, Long monsterId, String comment) throws NotFoundException {
        TasteList tasteList = getTasteListByUserId(userId);
        Monster monster = monsterRepository.findById(monsterId)
                .orElseThrow(() -> new NotFoundException("Monster non trovato"));

        TasteListItem item = tasteList.getItems().stream()
                .filter(i -> i.getMonster().getId().equals(monsterId))
                .findFirst()
                .orElseGet(() -> {
                    TasteListItem newItem = new TasteListItem();
                    newItem.setTasteList(tasteList);
                    newItem.setMonster(monster);
                    tasteList.getItems().add(newItem);
                    return newItem;
                });

        item.setComment(comment);
        tasteListItemRepository.save(item);
        return item;
    }

    public TasteList getTasteListByUserId(Long userId) throws NotFoundException {
        return tasteListRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("TasteList non trovata per user " + userId));
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
