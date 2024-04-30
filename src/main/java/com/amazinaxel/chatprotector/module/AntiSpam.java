package com.amazinaxel.chatprotector.module;

import com.amazinaxel.chatprotector.ChatProtector;
import com.amazinaxel.chatprotector.interfaces.Check;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AntiSpam implements Check {
    @Override
    public void executeControl(AsyncPlayerChatEvent event, ChatProtector chatProtector) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            return;
        }

        UUID uuid = player.getUniqueId();
        String message = event.getMessage();
        if (!chatProtector.getSharedCore().getRepeatedMessages().getCachedPlayerMessage(uuid)) {
            chatProtector.getSharedCore().getRepeatedMessages().addMessage(uuid, message);
            return;
        }
        if (!chatProtector.getSharedCore().getRepeatedMessages().getCachedCurrentMessage(uuid, message)) {
            chatProtector.getSharedCore().getRepeatedMessages().addMessage(uuid, message);
            return;
        }
        if (message.equals(chatProtector.getSharedCore().getRepeatedMessages().getMessage().get(uuid))) {
            player.sendMessage(Component.text("§cPlease don't repeat a similar message!"));
            Bukkit.getScheduler().runTaskLater(chatProtector, () -> chatProtector.getSharedCore().getRepeatedMessages().removeMessage(uuid, chatProtector.getSharedCore().getRepeatedMessages().getMessage().get(uuid)), 1000L);
            event.setCancelled(true);
        }
    }
}