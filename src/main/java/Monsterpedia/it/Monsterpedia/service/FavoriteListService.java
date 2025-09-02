package Monsterpedia.it.Monsterpedia.service;

import Monsterpedia.it.Monsterpedia.dto.response.FavoriteListDto;
import Monsterpedia.it.Monsterpedia.dto.response.FavoriteListItemDto;
import Monsterpedia.it.Monsterpedia.exception.NotFoundException;
import Monsterpedia.it.Monsterpedia.model.*;
import Monsterpedia.it.Monsterpedia.repository.FavoriteListRepository;
import Monsterpedia.it.Monsterpedia.repository.MonsterRepository;
import Monsterpedia.it.Monsterpedia.repository.TasteListItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteListService {

    @Autowired UserService userService;
    @Autowired FavoriteListRepository favoriteListRepository;
    @Autowired MonsterRepository monsterRepository;
    @Autowired TasteListItemRepository tasteListItemRepository;

    public FavoriteListDto viewFavoriteList(Long userId) throws NotFoundException {
        FavoriteList favoriteList = favoriteListRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Favorite non trovata per user " + userId));
        FavoriteListDto favoriteListDto = new FavoriteListDto();
        favoriteListDto.setFavoriteListId(favoriteList.getId());
        favoriteListDto.setItems(
                favoriteList.getItems().stream()
                        .map(item -> toDto(item, userId))
                        .toList()
        );
        return favoriteListDto;
    }


    public List<FavoriteListItem> getFavoriteListItems(long userId) throws NotFoundException {
        FavoriteList favoriteList = favoriteListRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("FavoriteList non trovata per user " + userId));
        return favoriteList.getItems();
    }

    private FavoriteListItemDto toDto(FavoriteListItem i, Long userId) {
        FavoriteListItemDto flid = new FavoriteListItemDto();
        Monster monster = i.getMonster();

        flid.setMonsterId(monster.getId());
        flid.setMonsterName(monster.getName());
        flid.setImageUrl(monster.getImageUrl());

        tasteListItemRepository.findByTasteListUserIdAndMonsterId(userId, monster.getId())
                .ifPresent(taste -> {
                    flid.setRating(taste.getRating());
                    flid.setTier(taste.getTier());
                    flid.setComment(taste.getComment());
                });
        return flid;
    }

    @Transactional
    public FavoriteListDto addItemToFavoriteList(long userId, long monsterId) throws NotFoundException {
        User u = userService.getUser(userId);
        Monster m = monsterRepository.findById(monsterId)
                .orElseThrow(() -> new NotFoundException("Monster non trovato: " + monsterId));

        FavoriteList favoriteList = favoriteListRepository.findByUserId(userId)
                .orElseGet(() -> {
                    FavoriteList fl = new FavoriteList();
                    fl.setUser(u);
                    return fl;
                });

        Optional<FavoriteListItem> existingFavorite = favoriteList.getItems().stream()
                .filter(i -> i.getMonster().getId().equals(monsterId))
                .findAny();

        FavoriteListItem favoriteItem = existingFavorite.orElseGet(() -> {
            FavoriteListItem fli = new FavoriteListItem();
            fli.setFavoriteList(favoriteList);
            fli.setMonster(m);
            favoriteList.getItems().add(fli);
            return fli;
        });

        tasteListItemRepository.findByTasteListUserIdAndMonsterId(userId, monsterId)
                .orElseGet(() -> {
                    TasteList tasteList = userService.getTasteList(u);
                    TasteListItem tli = new TasteListItem();
                    tli.setTasteList(tasteList);
                    tli.setMonster(m);
                    tli.setRating(0);
                    tli.setComment("");
                    tasteList.getItems().add(tli);

                    tasteListItemRepository.save(tli);
                    return tli;
                });

        favoriteListRepository.save(favoriteList);
        return viewFavoriteList(userId);
    }

    @Transactional
    public FavoriteListDto removeItemFromFavoriteList(Long userId, Long monsterId) throws NotFoundException {
        FavoriteList favoriteList = favoriteListRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("FavoriteList non trovata per user " + userId));
        favoriteList.getItems().removeIf(i -> i.getMonster().getId().equals(monsterId));
        favoriteListRepository.save(favoriteList);
        return viewFavoriteList(userId);
    }
}
