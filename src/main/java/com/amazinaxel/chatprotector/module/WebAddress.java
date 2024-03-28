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
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;
import java.util.List;

public class WebAddress
implements Check {
    @Override
    public void executeControl(AsyncPlayerChatEvent event, ChatProtector chatProtector) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            return;
        }
        String message = event.getMessage();
        for (String combofy: message.split(" ")) {
            // old & more reliable (but more false positives) regex: "(http\\:\\/\\/|https\\:\\/\\/)?([a-z0-9\\-\\s\\.\\W\\s\\S\\_]*\\.)+[a-z0-9\\s][a-z0-9\\-\\s\\.]*", "^(.* + \\W|\\d|_|.*|)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)+( .*)?$", "([a-z\\_\\S]*[\\#\\.\\$\\@\\!|,|\\_|\\€]+(\\w{2,4}))"
            List<String> websiteRegex = Arrays.asList("(http|https)[\\w|\\_|\\W|\\_]*(\\.)*(com|eu|_|de|info|cz|io|COM|EU|DE|INFO|CZ|IO)", "^(.* + \\W|\\d|_|.*|)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)+( .*)?$");

            for (String web_regex: websiteRegex) {
                if (!combofy.matches(web_regex)) continue;
                player.sendMessage("§cSharing website links are not permitted.");
                event.setCancelled(true);
                return;
            }
        }
    }
}

