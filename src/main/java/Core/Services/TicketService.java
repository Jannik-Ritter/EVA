package Core.Services;

import Core.Interfaces.TicketServiceInterface;
import Core.Models.exceptions.CustomerException;
import Core.Models.exceptions.TicketException;
import Core.Models.exceptions.EventException;
import Core.Models.Ticket;
import Core.Models.Customer;
import Core.Models.Event;

import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;

public class TicketService implements TicketServiceInterface {
    private ConcurrentHashMap<UUID, Ticket> tickets = new ConcurrentHashMap<>();

    private final int MAX_TICKETS_PER_CUSTOMER = 5;

    private CustomerService customerService;
    private EventService eventService;

    @Override
    public Ticket createTicket(UUID customerId, UUID eventId) throws CustomerException, EventException, TicketException {
        if (customerId == null && eventId == null) {
            throw CustomerException.customerDoesNotExist();
        }

        if (customerId == null) {
            throw CustomerException.customerDoesNotExist();
        }
        if (eventId == null) {
            throw EventException.eventDoesNotExist();
        }

        Customer customer = customerService.getCustomerById(customerId);
        Event event = eventService.getEventById(eventId);
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(event.getTime())) {
            throw TicketException.eventHasAlreadyHappened();
        }

        int ticketsBought = customer.getTicketsBought().size();
        if (ticketsBought >= MAX_TICKETS_PER_CUSTOMER) {
            throw TicketException.maximumNumberOfTickets();
        }

        if (event.getTicketsAvailable().get() <= 0) {
            throw TicketException.maximumNumberOfTickets();
        }
        
        UUID id = UUID.randomUUID();
        Ticket newTicket = new Ticket(id, customerId, eventId, LocalDate.now());

        eventService.addTicketSold(eventId, id);
        customerService.addTicketBought(customerId, id);

        tickets.put(id, newTicket);

        return newTicket;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public Ticket getTicketById(UUID id) throws TicketException {
        if (!tickets.containsKey(id)) {
            throw TicketException.ticketDoesNotExist();
        }

        Ticket ticket = tickets.get(id);
        Ticket reflectedTicket = new Ticket(ticket.getId(), ticket.getCustomerId(), ticket.getEventId(), ticket.getDateOfPurchase());
        return reflectedTicket;
    }

    @Override
    public List<Ticket> getAllTickets() {
        List<Ticket> allTickets = new ArrayList<>();
        for (Ticket ticket : tickets.values()) {
            Ticket reflectedTicket = getTicketById(ticket.getId());
            allTickets.add(reflectedTicket);
        }
        return allTickets;
    }

    @Override
    public void deleteTicket(UUID ticketId) throws IllegalArgumentException {
        if (ticketId == null) {
            throw new IllegalArgumentException("Ticket ID cannot be null");
        }

        if (!tickets.containsKey(ticketId)) {
            throw TicketException.ticketDoesNotExist();
        }

        Ticket ticket = tickets.get(ticketId);
        customerService.removeTicketBought(ticket.getCustomerId(), ticketId);
        eventService.removeTicketSold(ticket.getEventId(), ticketId);
        tickets.remove(ticketId);
    }

    @Override
    public void deleteAllTickets() {
        ArrayList<UUID> ticketIdsSnapshot = new ArrayList<>(tickets.keySet());
        for (UUID id : ticketIdsSnapshot) {
            deleteTicket(id);
        }
    }
}
