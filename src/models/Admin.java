package models;

import java.util.*;

public class Admin extends User {
    private Set<String> privileges;

    public Admin(String userId, String name, String email, String phone) {
        super(userId, name, email, phone);
        this.privileges = new HashSet<>(Arrays.asList(
            "ADD_MOVIE", "ADD_THEATER", "ADD_SHOW", "UPDATE_PRICING"
        ));
    }

    public boolean hasPrivilege(String privilege) {
        return privileges.contains(privilege);
    }
}
