package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto create(@RequestBody @Valid final ItemDto itemDto,
                                  @Positive @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("POST /items - создание вещи пользователем ID={}", ownerId);
        ItemResponseDto created = itemService.create(itemDto, ownerId);
        log.debug("Вещь создана: {}", created);
        return created;
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(@Positive @PathVariable Long itemId,
                                  @Valid @RequestBody ItemUpdateDto updateDto,
                                  @Positive @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("PATCH /items/{} - обновление вещи пользователем ID={}", itemId, ownerId);
        ItemResponseDto updated = itemService.update(itemId, updateDto, ownerId);
        log.debug("Вещь обновлена: {}", updated);
        return updated;
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getById(@Positive @PathVariable Long itemId,
                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET /items/{} - запрос вещи пользователем ID={}", itemId, userId);
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemResponseDto> getItemsByOwner(@Positive @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("GET /items - получение всех вещей пользователя ID={}", ownerId);
        return itemService.getItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchAvailable(@RequestParam String text) {
        log.info("GET /items/search - поиск вещей по тексту '{}'", text);
        return itemService.searchAvailable(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@PathVariable Long itemId,
                                         @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Valid @RequestBody CommentDto commentDto) {
        log.info("POST /items/{}/comment - пользователь ID={} оставляет комментарий", itemId, userId);
        return itemService.addComment(itemId, userId, commentDto);
    }
}