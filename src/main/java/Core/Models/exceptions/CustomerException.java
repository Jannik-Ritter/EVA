package Core.Models.exceptions;

public class CustomerException extends RuntimeException {
    public static final String underageCustomer = "User has to be 18 years old";
    public static final String invalidEmail = "Invalid email";
    public static final String emailWithMultipleAtSymbols = "Email must contain only one @ symbol";
    public static final String emailAlreadyExists = "Email already exists";
    public static final String usernameAlreadyExists = "Username already exists";
    public static final String customerDoesNotExist = "Customer does not exist";
    public static final String customerAlreadyExists = "Customer already exists";
    public static final String customerIdDoesNotExist = "Customer ID does not exist";

    public CustomerException(String message) {
        super(message);
    }

    public static CustomerException underageCustomer() {
        return new CustomerException(underageCustomer);
    }

    public static CustomerException invalidEmail() {
        return new CustomerException(invalidEmail);
    }

    public static CustomerException emailWithMultipleAtSymbols() {
        return new CustomerException(emailWithMultipleAtSymbols);
    }

    public static CustomerException emailAlreadyExists() {
        return new CustomerException(emailAlreadyExists);
    }

    public static CustomerException usernameAlreadyExists() {
        return new CustomerException(usernameAlreadyExists);
    }

    public static CustomerException customerDoesNotExist() {
        return new CustomerException(customerDoesNotExist);
    }
    
    public static CustomerException customerAlreadyExists() {
        return new CustomerException(customerAlreadyExists);
    }

    public static CustomerException customerIdDoesNotExist() {
        return new CustomerException(customerIdDoesNotExist);
    }
}
