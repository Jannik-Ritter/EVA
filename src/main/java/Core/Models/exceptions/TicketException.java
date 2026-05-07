package Core.Models.exceptions;

public class TicketException extends RuntimeException {
    public static final String ticketDoesNotExist = "Ticket does not exist";
    public static final String maximumNumberOfTickets = "Maximum number of tickets reached";
    public static final String eventHasAlreadyHappened = "Event has already happened";
    public static final String ticketIdDoesNotExist = "Ticket ID does not exist";
    public static final String customerIdOrEventIdCannotBeNull = "Customer ID or Event ID cannot be null";

    public TicketException(String message) {
        super(message);
    }

    public static TicketException ticketDoesNotExist() {
        return new TicketException(ticketDoesNotExist);
    }

    public static TicketException maximumNumberOfTickets() {
        return new TicketException(maximumNumberOfTickets);
    }

    public static TicketException eventHasAlreadyHappened() {
        return new TicketException(eventHasAlreadyHappened);
    }

    public static TicketException ticketIdDoesNotExist() {
        return new TicketException(ticketIdDoesNotExist);
    }

    public static TicketException customerIdOrEventIdCannotBeNull() {
        return new TicketException(customerIdOrEventIdCannotBeNull);
    }
}
