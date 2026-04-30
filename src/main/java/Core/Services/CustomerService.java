package Core.Services;

import Core.Interfaces.CustomerServiceInterface;
import Core.Models.Customer;
import Core.Models.exceptions.CustomerException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerService implements CustomerServiceInterface {
    private ConcurrentHashMap<UUID, Customer> customers = new ConcurrentHashMap<>();
    private TicketService ticketService;

    public CustomerService() {
        this.ticketService = new TicketService();
    }

    public CustomerService(TicketService ticketService) {
        setTicketService(ticketService);
    }

    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    private static void validateAdult(LocalDate dateOfBirth) {
        if (dateOfBirth.isAfter(LocalDate.now().minusYears(18))) {
            throw CustomerException.underageCustomer();
        }
    }

    private static void validateEmail(String email) {
        if (email == null) {
            throw CustomerException.invalidEmail();
        }
        int at = email.indexOf('@');
        if (at <= 0 || at == email.length() - 1) {
            throw CustomerException.invalidEmail();
        }
        int atAmount = 0;
        for (char c : email.toCharArray()) {
            if (c == '@') {
                atAmount++;
            }
        }
        if (atAmount != 1) {
            throw CustomerException.invalidEmail();
        }
    }

    @Override
    public Customer createCustomer(String username, String email, LocalDate dateOfBirth) throws CustomerException {
        validateAdult(dateOfBirth);
        validateEmail(email);

        UUID id = UUID.randomUUID();
        final Customer newCustomer = new Customer(id, username, email, dateOfBirth);

        customers.put(id, newCustomer);
        return getCustomerById(id);
    }
    
    @Override
    public Customer getCustomerById(UUID id) throws CustomerException {
        if (!customers.containsKey(id)) {
            throw CustomerException.customerDoesNotExist();
        }
        Customer customer = customers.get(id);
        return new Customer(
            id,
            customer.getUsername(),
            customer.getEmail(),
            customer.getDateOfBirth(),
            customer.getTicketsBought()
        );
    }

    @Override
    public void updateCustomer(Customer customer) throws CustomerException {
        UUID id = customer.getId();
        if (!customers.containsKey(id)) {
            throw CustomerException.customerDoesNotExist();
        }
        validateAdult(customer.getDateOfBirth());
        validateEmail(customer.getEmail());
        customers.put(id, customer);
    }
    
    @Override
    public void deleteCustomer(UUID id) throws CustomerException {
        if (!customers.containsKey(id)) {
            throw CustomerException.customerDoesNotExist();
        }
        Customer customer = getCustomerById(id);
        for (UUID ticketId : customer.getTicketsBought()) {
            ticketService.deleteTicket(ticketId);
        }
        customers.remove(id);
    }
    
    
    @Override
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }
    
    @Override
    public void deleteAllCustomers() {
        for (Customer customer : customers.values()) {
            deleteCustomer(customer.getId());
        }
    }

    @Override
    public void addTicketBought(UUID customerId, UUID ticketId) {
        Customer customer = getCustomerById(customerId);
        customer.addTicketBought(ticketId);

        updateCustomer(customer);
    }

    @Override
    public void removeTicketBought(UUID customerId, UUID ticketId) {
        Customer customer = getCustomerById(customerId);
        customer.removeTicketBought(ticketId);

        updateCustomer(customer);
    }
}
