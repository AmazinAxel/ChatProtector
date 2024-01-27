/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 */
package com.amazinaxel.chatprotector.listeners;

import com.amazinaxel.chatprotector.ChatProtector;
import com.amazinaxel.chatprotector.interfaces.Check;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncChat
implements Listener {
    private final ChatProtector reloaded;
    private final Check check;

    public AsyncChat(ChatProtector reloaded, Check check) {
        this.reloaded = reloaded;
        this.check = check;
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void asyncChat(AsyncPlayerChatEvent event) {
        this.check.executeControl(event, this.reloaded);
    }
}

