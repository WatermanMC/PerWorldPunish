package com.github.WatermanMC.PerWorldPunish;

import com.github.WatermanMC.PerWorldPunish.commands.*;
import com.github.WatermanMC.PerWorldPunish.managers.*;
import org.bukkit.plugin.java.JavaPlugin;
import io.papermc.paper.plugin.configuration.PluginMeta;
import java.util.*;

public class PerWorldPunish extends JavaPlugin {
    private static PerWorldPunish instance;
    private ConfigManager configManager;
    private DataManager dataManager;
    private Map<UUID, Set<WorldBan>> bans;

    @Override
    public void onEnable() {
        PluginMeta  pluginMeta = getPluginMeta();
        instance = this;
        this.configManager = new ConfigManager();
        this.dataManager = new DataManager();
        this.bans = new HashMap<>();
        loadData();
        registerCommands();
        registerEvents();
        getLogger().info("PerWorldPunish v" + pluginMeta.getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        PluginMeta  pluginMeta = getPluginMeta();
        saveData();
        getLogger().info("PerWorldPunish v" + pluginMeta.getVersion() + " disabled!");
    }

    private void registerCommands() {
        new WorldBanCommand(this);
        new WorldUnbanCommand(this);
        new WorldBanListCommand(this);
        new WorldKickCommand(this);
        new WorldTempBanCommand(this);
        new PerWorldPunishCommand(this);
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new BanListener(this), this);
    }

    private void loadData() {
        bans = dataManager.loadBans();
    }

    private void saveData() {
        dataManager.saveBans(bans);
    }

    public static PerWorldPunish getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }


    public Map<UUID, Set<WorldBan>> getBans() {
        return bans;
    }

    public void addBan(UUID playerId, WorldBan ban) {
        bans.computeIfAbsent(playerId, k -> new HashSet<>()).add(ban);
    }

    public void removeBan(UUID playerId, String worldName) {
        if (bans.containsKey(playerId)) {
            bans.get(playerId).removeIf(ban -> ban.getWorld().equalsIgnoreCase(worldName));
            if (bans.get(playerId).isEmpty()) {
                bans.remove(playerId);
            }
        }
    }

    public boolean isBanned(UUID playerId, String worldName) {
        if (!bans.containsKey(playerId)) return false;

        for (WorldBan ban : bans.get(playerId)) {
            if (ban.getWorld().equalsIgnoreCase(worldName)) {
                if (ban.isTemporary()) {
                    if (System.currentTimeMillis() > ban.getExpiryTime()) {
                        removeBan(playerId, worldName);
                        return false;
                    }
                    return true;
                }
                return true;
            }
        }
        return false;
    }
}