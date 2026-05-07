package Core.Models;

import java.time.LocalDate;
import java.util.UUID;

public class Ticket {
    private final UUID id;
    private final UUID customerId;
    private final UUID eventId;
    private final LocalDate dateOfPurchase;

    public Ticket(UUID id, UUID customerId, UUID eventId, LocalDate dateOfPurchase) {
        this.id = id;
        this.customerId = customerId;
        this.eventId = eventId;
        this.dateOfPurchase = dateOfPurchase;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }
}
