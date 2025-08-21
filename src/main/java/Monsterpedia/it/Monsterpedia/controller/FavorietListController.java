package Monsterpedia.it.Monsterpedia.controller;

import Monsterpedia.it.Monsterpedia.dto.request.AddToFavoriteListRequestDto;
import Monsterpedia.it.Monsterpedia.dto.response.FavoriteListDto;
import Monsterpedia.it.Monsterpedia.exception.NotFoundException;
import Monsterpedia.it.Monsterpedia.service.FavoriteListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users/{userId}/favorites")
public class FavorietListController {
    @Autowired private FavoriteListService favoriteListService;

    @GetMapping
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<FavoriteListDto> viewFavoriteList(@PathVariable Long userId) throws NotFoundException {
        return ResponseEntity.ok(favoriteListService.viewFavoriteList(userId));
    }

    @PostMapping
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<FavoriteListDto> addItemToFavoriteList(
            @PathVariable Long userId,
            @RequestBody @Valid AddToFavoriteListRequestDto requestDto
            ) throws NotFoundException {
        return ResponseEntity.ok(
                favoriteListService.addItemToFavoriteList(userId, requestDto.getMonsterId())
        );
    }

    @DeleteMapping("/{monsterId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<FavoriteListDto> removeToFavoriteList(
            @PathVariable Long userId,
            @PathVariable Long monsterId
    ) throws NotFoundException {
        return ResponseEntity.ok(favoriteListService.removeItemFromFavoriteList(userId, monsterId));
    }
}
