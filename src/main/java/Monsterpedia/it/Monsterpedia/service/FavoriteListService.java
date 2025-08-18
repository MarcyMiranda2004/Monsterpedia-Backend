package Monsterpedia.it.Monsterpedia.service;

import Monsterpedia.it.Monsterpedia.dto.response.FavoriteListDto;
import Monsterpedia.it.Monsterpedia.dto.response.FavoriteListItemDto;
import Monsterpedia.it.Monsterpedia.exception.NotFoundException;
import Monsterpedia.it.Monsterpedia.model.FavoriteList;
import Monsterpedia.it.Monsterpedia.model.FavoriteListItem;
import Monsterpedia.it.Monsterpedia.model.Monster;
import Monsterpedia.it.Monsterpedia.model.User;
import Monsterpedia.it.Monsterpedia.repository.FavoriteListRepository;
import Monsterpedia.it.Monsterpedia.repository.MonsterRepository;
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

    public FavoriteListDto viewFavoriteList(Long userId) throws NotFoundException {
        FavoriteList favoriteList = favoriteListRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Favorite non trovata per user " + userId));
        FavoriteListDto favoriteListDto = new FavoriteListDto();
        favoriteListDto.setFavoriteListId(favoriteList.getId());
        favoriteListDto.setItems(favoriteList.getItems().stream().map(this::toDto).toList());
        return favoriteListDto;
    }

    public List<FavoriteListItem> getFavoriteListItems(long userId) throws NotFoundException {
        FavoriteList favoriteList = favoriteListRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("FavoriteList non trovata per user " + userId));
        return favoriteList.getItems();
    }

    private FavoriteListItemDto toDto(FavoriteListItem i) {
        FavoriteListItemDto flid = new FavoriteListItemDto();
        flid.setMonsterId(flid.getMonsterId());
        flid.setMonsterName(flid.getMonsterName());
        flid.setImageUrl(flid.getImageUrl());
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
        Optional<FavoriteListItem> existing = favoriteList.getItems().stream()
                .filter(i -> i.getMonster().getId().equals(monsterId)).findAny();
        FavoriteListItem item = existing.orElseGet(() -> {
            FavoriteListItem fli = new FavoriteListItem();
            fli.setFavoriteList(favoriteList);
            fli.setMonster(m);
            favoriteList.getItems().add(fli);
            return fli;
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
