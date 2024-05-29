package com.se14.repository.db_impl;

import java.util.UUID;

public class UniqueIDGenerator {
    public static int generateUniqueId() {
        UUID uuid = UUID.randomUUID();
        // Extracting the hash code of the UUID as an integer
        int id = uuid.hashCode();
        // Handling the possibility of negative hash codes
        if (id == Integer.MIN_VALUE) {
            id = 0; // Avoid potential issues with negative IDs
        } else {
            id = Math.abs(id); // Make sure ID is positive
        }
        return id;
    }
}
