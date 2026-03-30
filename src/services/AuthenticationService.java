package services;

import models.*;
import exceptions.UnauthorizedAccessException;

public class AuthenticationService {
    public boolean authenticate(User user, String password) {
        // Simulate authentication (in production, use hashed passwords)
        return user != null;
    }

    public void authorize(User user, String action) throws UnauthorizedAccessException {
        if (user instanceof Admin) {
            Admin admin = (Admin) user;
            if (!admin.hasPrivilege(action)) {
                throw new UnauthorizedAccessException("Admin does not have privilege: " + action);
            }
        } else if (user instanceof Customer) {
            // Customers can perform booking-related actions
            if (!action.startsWith("BOOK_")) {
                throw new UnauthorizedAccessException("Customer cannot perform: " + action);
            }
        }
    }

    public boolean isAdmin(User user) {
        return user instanceof Admin;
    }
}
