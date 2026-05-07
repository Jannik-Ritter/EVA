package Core.Interfaces;

import Core.Models.Customer;
import Core.Models.exceptions.CustomerException;
import Core.Services.TicketService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CustomerServiceInterface {
    void setTicketService(TicketService ticketService);

    Customer createCustomer(String username, String email, LocalDate dateOfBirth) throws CustomerException;
    Customer getCustomerById(UUID id) throws CustomerException;

    void updateCustomer(Customer customer) throws CustomerException;
    void deleteCustomer(UUID id) throws CustomerException;
    
    List<Customer> getAllCustomers();
    void deleteAllCustomers();
    
    void addTicketBought(UUID customerId, UUID ticketId);
    void removeTicketBought(UUID customerId, UUID ticketId);
}
