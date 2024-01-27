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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AntiSwear implements Check {
    @Override
    public void executeControl(AsyncPlayerChatEvent event, ChatProtector chatProtector) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            return;
        }
        try {
            Scanner sc = new Scanner(new File(chatProtector.getDataFolder(), "blockedwords.txt"));
            StringBuilder sb = new StringBuilder();
            String originalMessage = event.getMessage();
            String message = originalMessage.toLowerCase();
            while (sc.hasNext()) {
                String s = sc.next();
                String patternString = "(?i)" + s + "[^ ]*";
                Pattern pattern = Pattern.compile(patternString);
                Matcher matcher = pattern.matcher(originalMessage);
                while (matcher.find()) {
                    String replacement = "*".repeat(matcher.group().length());
                    matcher.appendReplacement(sb, replacement);
                }
                matcher.appendTail(sb);
                message = sb.toString();
                sb.setLength(0);
            }
            event.setMessage(message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}