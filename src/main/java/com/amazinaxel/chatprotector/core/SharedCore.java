/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.kyori.adventure.text.TextComponent
 *  net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
 *  net.md_5.bungee.api.ChatColor
 *  net.md_5.bungee.api.ProxyServer
 *  net.md_5.bungee.api.plugin.Plugin
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.plugin.Plugin
 */
package com.amazinaxel.chatprotector.core;

import com.amazinaxel.chatprotector.core.cache.RepeatedMessages;

public class SharedCore {
    public RepeatedMessages repeatedMessages = new RepeatedMessages();
    public RepeatedMessages getRepeatedMessages() {
        return this.repeatedMessages;
    }

}

