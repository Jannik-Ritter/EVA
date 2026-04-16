package Core.Services;

import Core.Models.exceptions.EventException;
import Core.Interfaces.EventServiceInterface;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import Core.Models.Event;

public class EventService implements EventServiceInterface {

    private ConcurrentHashMap<UUID, Event> events = new ConcurrentHashMap<>();

    public Event createEvent(String name, String location, LocalDateTime time, int ticketsAvailable) throws EventException {
        UUID id = UUID.randomUUID();
        Event newEvent = new Event(id, name, location, time, ticketsAvailable);

        events.put(id, newEvent);
        return newEvent;
    }

    @Override
    public Event getEventById(UUID id) {
        return events.get(id);
    }

    @Override
    public void updateEvent(Event event) throws EventException {
        try{
            UUID id = event.getId();
            events.put(id, event);
            validateUpdatedEvent(event);
        } catch (EventException e) {
            throw e;
        }
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
