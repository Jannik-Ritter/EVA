package Core.Interfaces;

import Core.Models.Event;
import Core.Services.TicketService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventServiceInterface {
    void setTicketService(TicketService ticketService);

    Event createEvent(String name, String location, LocalDateTime time, int ticketsAvailable) throws IllegalArgumentException;

    Event getEventById(UUID id);
    List<Event> getAllEvents();

    void updateEvent(Event event) throws IllegalArgumentException;

    void deleteEvent(UUID id) throws IllegalArgumentException;
    void deleteAllEvents();

    void addTicketSold(UUID eventId, UUID ticketId);
    void removeTicketSold(UUID eventId, UUID ticketId);
}
