package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createBooking(@RequestBody @Valid BookingDto bookingDto,
                                            @Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("POST /bookings by userId {}", userId);
        return bookingService.createBooking(bookingDto, userId);
    }


    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@Positive @PathVariable Long bookingId,
                                             @Positive @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                             @RequestParam boolean approved) {
        log.info("PATCH /bookings/{}?approved={} by ownerId {}", bookingId, approved, ownerId);
        return bookingService.approveBooking(bookingId, ownerId, approved);
    }


    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@Positive @PathVariable Long bookingId,
                                             @Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET /bookings/{} by userId {}", bookingId, userId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> getBookingsByBooker(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam String state,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        log.info("GET /bookings by bookerId {}, state={}, from={}, size={}", userId, state, from, size);
        return bookingService.getBookingsByBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsByOwner(@Positive @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                       @RequestParam(defaultValue = "ALL") String state,
                                                       @RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size) {
        log.info("GET /bookings/ownerId by ownerId {}, state={}, from={}, size={}", ownerId, state, from, size);
        return bookingService.getBookingsByOwner(ownerId, state, from, size);
    }
}