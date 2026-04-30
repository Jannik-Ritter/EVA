package Core.Interfaces;

import java.util.List;
import java.util.UUID;

import Core.Models.Ticket;
import Core.Models.exceptions.TicketException;
import Core.Services.CustomerService;
import Core.Services.EventService;

public interface TicketServiceInterface {
    void setCustomerService(CustomerService customerService);
    void setEventService(EventService eventService);

    Ticket createTicket(UUID customerId, UUID eventId) throws TicketException;

    Ticket getTicketById(UUID id) throws TicketException;

    List<Ticket> getAllTickets();

    void deleteTicket(UUID id) throws TicketException;

    void deleteAllTickets();
}
