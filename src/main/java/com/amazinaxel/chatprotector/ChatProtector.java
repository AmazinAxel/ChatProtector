package com.amazinaxel.chatprotector;

import com.amazinaxel.chatprotector.core.SharedCore;
import com.amazinaxel.chatprotector.listeners.AsyncChat;
import com.amazinaxel.chatprotector.listeners.PlayerQuit;
import com.amazinaxel.chatprotector.module.AntiSpam;
import com.amazinaxel.chatprotector.module.AntiSwear;
import com.amazinaxel.chatprotector.module.IPAddress;
import com.amazinaxel.chatprotector.module.WebAddress;

import java.io.File;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatProtector
extends JavaPlugin {
    private final Logger logger = this.getLogger();
    private final SharedCore sharedCore = new SharedCore();
    private final PluginManager manager = this.getServer().getPluginManager();

    public void onEnable() {
        this.logger.info("ChatProtector has been enabled.");
        this.createBadWords();

        // Register events
        this.manager.registerEvents(new AsyncChat(this, new AntiSpam()), this);
        this.manager.registerEvents(new AsyncChat(this, new AntiSwear()), this);
        this.manager.registerEvents(new AsyncChat(this, new IPAddress()), this);
        this.manager.registerEvents(new AsyncChat(this, new WebAddress()), this);
        this.manager.registerEvents(new PlayerQuit(this), this);
    }

    public void onDisable() {
        this.logger.info("ChatProtector has been disabled.");
    }

    public SharedCore getSharedCore() {
        return this.sharedCore;
    }

    public void createBadWords() {
        File file = new File(this.getDataFolder(), "blockedwords.txt");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            this.saveResource("blockedwords.txt", false);
        }
    }
}

