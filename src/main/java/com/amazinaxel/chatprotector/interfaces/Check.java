/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 */
package com.amazinaxel.chatprotector.interfaces;

import com.amazinaxel.chatprotector.ChatProtector;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public interface Check {
    void executeControl(AsyncPlayerChatEvent var1, ChatProtector var2);
}

