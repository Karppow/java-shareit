package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
@Validated
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestResponseDto createRequest(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                                @Valid @RequestBody final ItemRequestDto dto) {
        log.info("POST /requests by userId {}", userId);
        return requestService.create(userId, dto);
    }


    @GetMapping
    public List<ItemRequestResponseDto> getOwnRequests(@Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET /requests (own) by userId {}", userId);
        return requestService.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getAllRequests(@Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET /requests/all by userId {}", userId);
        return requestService.getAllRequests(userId);
    }
}