/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 */
package com.amazinaxel.chatprotector.module;

import com.amazinaxel.chatprotector.ChatProtector;
import com.amazinaxel.chatprotector.interfaces.Check;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class IPAddress
implements Check {
    @Override
    public void executeControl(AsyncPlayerChatEvent event, ChatProtector chatProtector) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (player.isOp()) {
            return;
        }
        String ipRegex = "\\d{1,3}[.|#|,]?\\d{1,3}?[.|#|,]\\d{1,3}[.|#|,]\\d{1,3}|[:]\\d{1,5}";
        for (String messages : message.split(" ")) {
            if (!messages.matches(ipRegex)) continue;
            player.sendMessage(Component.text("§cSharing IP addresses are not permitted."));
            event.setCancelled(true);
        }
    }
}

