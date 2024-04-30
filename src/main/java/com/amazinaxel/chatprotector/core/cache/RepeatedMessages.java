package com.amazinaxel.chatprotector.core.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RepeatedMessages {
    public Map<UUID, String> message = new HashMap<>();

    public void addMessage(UUID uuid, String value) {
        this.message.put(uuid, value);
    }

    public void removeMessage(UUID uuid, String value) {
        this.message.remove(uuid, value);
    }

    public boolean getCachedPlayerMessage(UUID uuid) {
        return this.getMessage().containsKey(uuid);
    }

    public boolean getCachedCurrentMessage(UUID uuid, String message) {
        return this.getMessage().get(uuid).equalsIgnoreCase(message);
    }

    public Map<UUID, String> getMessage() {
        return this.message;
    }

    public synchronized String toString() {
        return "PlayerCache{message=" + this.message + "}";
    }
}

