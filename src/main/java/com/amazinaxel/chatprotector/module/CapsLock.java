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

public class CapsLock
implements Check {
    @Override
    public void executeControl(AsyncPlayerChatEvent event, ChatProtector chatProtector) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (player.isOp()) {
            return;
        }
        int upperChars = 0;
        int lowerChars = 0;
        for (char s: message.toCharArray()) {
            if (Character.isUpperCase(s)) { ++upperChars; }
            else if (Character.isLowerCase(s)) { ++lowerChars; }
        }
        if (upperChars > lowerChars) { // More uppercase characters than lowercase characters
            player.sendMessage(Component.text("§cYour message contained too many capital letters!"));
            event.setMessage(event.getMessage().toLowerCase());
        }
    }
}

