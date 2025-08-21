package Monsterpedia.it.Monsterpedia.controller;

import Monsterpedia.it.Monsterpedia.dto.request.AddTastedListRequestDto;
import Monsterpedia.it.Monsterpedia.dto.request.CommentRequestDto;
import Monsterpedia.it.Monsterpedia.dto.request.RatingRequestDto;
import Monsterpedia.it.Monsterpedia.dto.response.TastedListDto;
import Monsterpedia.it.Monsterpedia.exception.NotFoundException;
import Monsterpedia.it.Monsterpedia.model.TasteListItem;
import Monsterpedia.it.Monsterpedia.service.TastedListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/tasteds")
public class TastedListController {
    @Autowired private TastedListService tastedListService;

    @GetMapping
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<TastedListDto> viewTastedList(@PathVariable Long userId) {
        return ResponseEntity.ok(tastedListService.viewTastedList(userId));
    }

    @PostMapping
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<TastedListDto> addItemToTastedList(
            @PathVariable Long userId,
            @RequestBody @Valid AddTastedListRequestDto requestDto
    ) throws NotFoundException {
        return ResponseEntity.ok(
                tastedListService.addToTastedList(userId, requestDto.getMonsterId())
        );
    }

    @PutMapping("/monster/{monsterId}/rating")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<TasteListItem> setRating(
            @PathVariable Long userId,
            @PathVariable Long monsterId,
            @RequestBody RatingRequestDto request) {
        TasteListItem item = tastedListService.setRating(userId, monsterId, request.getRating());
        return ResponseEntity.ok(item);
    }

    @PutMapping("/monster/{monsterId}/comment")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<TasteListItem> setComment(
            @PathVariable Long userId,
            @PathVariable Long monsterId,
            @RequestBody CommentRequestDto commentRequest) {
        TasteListItem item = tastedListService.setComment(userId, monsterId, commentRequest.getComment());
        return ResponseEntity.ok(item);
    }


    @DeleteMapping("/{monsterId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public  ResponseEntity<TastedListDto> removeFromTastedList(
            @PathVariable Long userId,
            @PathVariable Long monsterId
    ) throws NotFoundException {
        return ResponseEntity.ok(tastedListService.removeItemFromTastedList(userId, monsterId));
    }
}
