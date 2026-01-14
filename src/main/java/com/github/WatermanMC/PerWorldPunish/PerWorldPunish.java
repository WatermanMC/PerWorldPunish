package com.github.WatermanMC.PerWorldPunish;

import com.github.WatermanMC.PerWorldPunish.managers.*;
import com.github.WatermanMC.PerWorldPunish.commands.*;
import org.bukkit.plugin.java.JavaPlugin;
import io.papermc.paper.plugin.configuration.PluginMeta;
import java.util.*;
import com.github.WatermanMC.PerWorldPunish.api.PerWorldPunishAPI;
import com.github.WatermanMC.PerWorldPunish.api.PlayerWorldBanEvent;
import com.github.WatermanMC.PerWorldPunish.api.PlayerWorldTempBanEvent;
import com.github.WatermanMC.PerWorldPunish.api.PlayerWorldUnbanEvent;
import com.github.WatermanMC.PerWorldPunish.api.PlayerWorldKickEvent;
import org.bukkit.Bukkit;
import java.util.*;

public class PerWorldPunish extends JavaPlugin {
    private static PerWorldPunish instance;
    private ConfigManager configManager;
    private DataManager dataManager;
    private Map<UUID, Set<WorldBan>> bans;
    private PerWorldPunishAPIImpl api;

    @Override
    public void onEnable() {
        PluginMeta meta = getPluginMeta();
        getLogger().info("Loading PerWorldPunish v" + meta.getVersion() + " data...");
        instance = this;
        this.api = new PerWorldPunishAPIImpl(this);
        this.configManager = new ConfigManager();
        this.dataManager = new DataManager();
        this.bans = new HashMap<>();
        getServer().getServicesManager().register(
                PerWorldPunishAPI.class,
                this.api,
                this,
                org.bukkit.plugin.ServicePriority.Normal
        );
        loadData();
        registerCommands();
        registerEvents();
        getLogger().info("Data loaded successfully!");
        getLogger().info("Enabled PerWorldPunish v" + meta.getVersion());
    }

    @Override
    public void onDisable() {
        PluginMeta meta = getPluginMeta();
        getLogger().info("Saving bans data...");
        saveData();
        getLogger().info("Saved bans data successfully!");
        getLogger().info("Disabled PerWorldPunish v" + meta.getVersion());
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

    public static PerWorldPunishAPI getApi() {
        return instance.api;
    }

    private void loadData() {
        bans = dataManager.loadBans();
    }

    private void saveData() {
        if (dataManager != null && bans != null) {
            dataManager.saveBans(bans, true);
        }
    }

    public static PerWorldPunish getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public Map<UUID, Set<WorldBan>> getBans() {
        return bans;
    }

    public void addBan(UUID playerId, WorldBan ban) {
        bans.computeIfAbsent(playerId, k -> new HashSet<>()).add(ban);
        dataManager.markDirty();
        dataManager.saveBans(bans, false);
    }

    public void removeBan(UUID playerId, String worldName) {
        if (bans.containsKey(playerId)) {
            bans.get(playerId).removeIf(ban -> ban.getWorld().equalsIgnoreCase(worldName));
            if (bans.get(playerId).isEmpty()) {
                bans.remove(playerId);
            }
            dataManager.markDirty();
            dataManager.saveBans(bans, false);
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

    public PerWorldPunishAPIImpl getAPI() {
        return api;
    }

    public PlayerWorldBanEvent callBanEvent(UUID playerId, String worldName, String reason) {
        PlayerWorldBanEvent event = new PlayerWorldBanEvent(playerId, worldName, reason);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public PlayerWorldTempBanEvent callTempBanEvent(UUID playerId, String worldName, long minutes, String reason) {
        PlayerWorldTempBanEvent event = new PlayerWorldTempBanEvent(playerId, worldName, minutes, reason);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public PlayerWorldUnbanEvent callUnbanEvent(UUID playerId, String worldName) {
        PlayerWorldUnbanEvent event = new PlayerWorldUnbanEvent(playerId, worldName);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public PlayerWorldKickEvent callKickEvent(UUID playerId, String worldName, String reason) {
        PlayerWorldKickEvent event = new PlayerWorldKickEvent(playerId, worldName, reason);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
}