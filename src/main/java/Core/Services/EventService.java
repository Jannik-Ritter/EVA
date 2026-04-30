package Core.Services;

import Core.Models.exceptions.EventException;
import Core.Interfaces.EventServiceInterface;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import Core.Models.Event;

public class EventService implements EventServiceInterface {
    private ConcurrentHashMap<UUID, Event> events = new ConcurrentHashMap<>();
    private TicketService ticketService;

    public EventService() {
        this.ticketService = new TicketService();
    }

    public EventService(TicketService ticketService) {
        setTicketService(ticketService);
    }

    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }
    public Event createEvent(String name, String location, LocalDateTime time, final int ticketsAvailable) throws EventException {
        UUID id = UUID.randomUUID();
        final Event newEvent = new Event(id, name, location, time, ticketsAvailable);

        events.put(id, newEvent);
        return getEventById(id);
    }

    @Override
    public Event getEventById(UUID id) throws EventException {
        if (events.containsKey(id) == false) {
            throw EventException.eventDoesNotExist();
        }

        Event event = events.get(id);
        return new Event(
            id,
            event.getName(),
            event.getLocation(),
            event.getTime(),
            event.getTicketsAvailable().get(),
            event.getTicketsSold()
        );
    }

    @Override
    public void updateEvent(Event event) throws EventException {
        updateEvent(event, true);
    }

    private void updateEvent(Event event, boolean enforceTicketContingentGuard) throws EventException {
        UUID id = event.getId();
        Event currentEvent = events.get(id);
        if (currentEvent == null) {
            throw EventException.eventDoesNotExist();
        }

        if (event.getTime().isBefore(LocalDateTime.now())) {
            throw EventException.cantSetEventTimeIntoPast();
        }

        if (
            enforceTicketContingentGuard &&
            event.getTicketsAvailable().get() < currentEvent.getTicketsAvailable().get()
        ) {
            throw EventException.shouldNotReduceAvailableTicketsWithUpdate();
        }

        events.put(id, event);
    }


    @Override
    public void deleteEvent(UUID id) {
        Event event = getEventById(id);
        for (UUID ticketId : event.getTicketsSold()) {
            ticketService.deleteTicket(ticketId);
        }
        
        events.remove(id);
    }

    @Override
    public List<Event> getAllEvents() {
        return new ArrayList<>(events.values());
    }

    @Override
    public void deleteAllEvents() {
        for (Event event : events.values()) {
            deleteEvent(event.getId());
        }
    }

    @Override
    public void addTicketSold(UUID eventId, UUID ticketId) {
        Event event = getEventById(eventId);
        event.addTicketSold(ticketId);
        event.updateTicketsAvailable(-1);

        updateEvent(event, false);
    }

    @Override
    public void removeTicketSold(UUID eventId, UUID ticketId) {
        Event event = getEventById(eventId);
        event.removeTicketSold(ticketId);
        event.updateTicketsAvailable(1);

        updateEvent(event, false);
    }


}
