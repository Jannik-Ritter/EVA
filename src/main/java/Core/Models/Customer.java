package Core.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer {
    private final UUID id;
    private String username;
    private String email;
    private LocalDate dateOfBirth;
    private List<UUID> ticketsBought;

    public Customer(UUID id, String username, String email, LocalDate dateOfBirth) {
        this(id, username, email, dateOfBirth, new ArrayList<UUID>());
    }

    public Customer(
        UUID id,
        String username,
        String email,
        LocalDate dateOfBirth,
        List<UUID> ticketsBought
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.ticketsBought = new ArrayList<UUID>(ticketsBought);
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public List<UUID> getTicketsBought() {
        return ticketsBought;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void addTicketBought(UUID ticketId) {
        ticketsBought.add(ticketId);
    }

    public void removeTicketBought(UUID ticketId) {
        ticketsBought.remove(ticketId);
    }
}
