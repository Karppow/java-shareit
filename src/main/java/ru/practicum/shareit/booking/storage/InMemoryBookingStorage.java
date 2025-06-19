package ru.practicum.shareit.booking.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static ru.practicum.shareit.booking.Booking.BookingStatus;


@Repository
public class InMemoryBookingStorage implements BookingStorage {
    private final Map<Long, Booking> bookings = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);


    @Override
    public Booking save(Booking booking) {
        long id = idGenerator.incrementAndGet();
        booking.setId(id);
        bookings.put(id, booking);
        return booking;
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return Optional.ofNullable(bookings.get(id));
    }


    @Override
    public Booking update(Booking booking) {
        Long id = booking.getId();
        if (id == null || !bookings.containsKey(id)) {
            throw new NoSuchElementException("Booking not found with id: " + id);
        }
        bookings.put(id, booking);
        return booking;
    }


    @Override
    public List<Booking> findByBookerId(Long bookerId, String state, int from, int size) {
        List<Booking> filtered = bookings.values().stream()
                .filter(b -> b.getBooker().getId().equals(bookerId))
                .filter(b -> filterByState(b, state))
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .toList();
        return paginate(filtered, from, size);
    }


    @Override
    public List<Booking> findByOwnerId(Long ownerId, String state, int from, int size) {
        List<Booking> filtered = bookings.values().stream()
                .filter(b -> b.getItem().getOwner().getId().equals(ownerId))
                .filter(b -> filterByState(b, state))
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .toList();
        return paginate(filtered, from, size);
    }


    private boolean filterByState(Booking booking, String state) {
        if (state == null || state.equalsIgnoreCase("ALL")) {
            return true;
        }
        BookingStatus status = booking.getStatus();
        LocalDateTime now = LocalDateTime.now();
        return switch (state.toUpperCase()) {
            case "WAITING" -> status == BookingStatus.WAITING;
            case "REJECTED" -> status == BookingStatus.REJECTED;
            case "APPROVED" -> status == BookingStatus.APPROVED;
            case "CANCELED" -> status == BookingStatus.CANCELED;
            case "CURRENT" -> booking.getStart().isBefore(now) && booking.getEnd().isAfter(now);
            case "PAST" -> booking.getEnd().isBefore(now);
            case "FUTURE" -> booking.getStart().isAfter(now);
            default -> false;
        };
    }


    private List<Booking> paginate(List<Booking> list, int from, int size) {
        int start = Math.min(from, list.size());
        int end = Math.min(from + size, list.size());
        return list.subList(start, end);
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }
    @Override
    public void deleteById(Long id) {
        bookings.remove(id);
    }
}