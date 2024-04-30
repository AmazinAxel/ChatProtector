package com.amazinaxel.chatprotector.listeners;

import com.amazinaxel.chatprotector.ChatProtector;

import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit
implements Listener {
    private final ChatProtector reloaded;

    public PlayerQuit(ChatProtector reloaded) {
        this.reloaded = reloaded;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (this.reloaded.getSharedCore().getRepeatedMessages().getCachedPlayerMessage(uuid)) {
            this.reloaded.getSharedCore().getRepeatedMessages().getMessage().remove(uuid);
        }
    }
}

