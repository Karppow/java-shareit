package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@Builder
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private BookingStatus status;

    public enum BookingStatus {
        WAITING,
        APPROVED,
        REJECTED,
        CANCELED
    }
}