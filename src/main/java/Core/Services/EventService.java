package Core.Services;

import Core.Models.exceptions.EventException;
import Core.Interfaces.EventServiceInterface;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import Core.Models.Event;

public class EventService implements EventServiceInterface {

    private ConcurrentHashMap<UUID, Event> events = new ConcurrentHashMap<>();

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
        Event reflectedEvent = new Event(id, event.getName(), event.getLocation(), event.getTime(), event.getTicketsAvailable().get());
        return reflectedEvent;
    }

    @Override
    public void updateEvent(Event event) throws EventException {
        UUID id = event.getId();
        if (!events.containsKey(id)) {
            throw EventException.eventDoesNotExist();
        }

        if (event.getTime().isBefore(LocalDateTime.now())) {
            throw EventException.cantSetEventTimeIntoPast();
        }

        if (event.getTicketsAvailable().get() < events.get(id).getTicketsAvailable().get()) {
            throw EventException.shouldNotReduceAvailableTicketsWithUpdate();
        }

        events.put(id, event);
        validateUpdatedEvent(event);
    }

    private void validateUpdatedEvent(Event event) throws EventException {
        boolean doesExist = events.containsKey(event.getId());
        if (!doesExist) {
            EventException.eventDoesNotExist();
        }
    }


    @Override
    public void deleteEvent(UUID id) {
        events.remove(id);
    }

    @Override
    public List<Event> getAllEvents() {
        return new ArrayList<>(events.values());
    }

    @Override
    public void deleteAllEvents() {
        events.clear();
    }


}
