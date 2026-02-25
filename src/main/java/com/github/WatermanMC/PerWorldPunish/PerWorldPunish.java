package com.github.WatermanMC.PerWorldPunish;

import com.github.WatermanMC.PerWorldPunish.commands.tabcompleter.*;
import com.github.WatermanMC.PerWorldPunish.commands.*;
import com.github.WatermanMC.PerWorldPunish.managers.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PerWorldPunish extends JavaPlugin {
    private ConfigManager configManager;
    private DataManager dataManager;
    private Map<UUID, Set<WorldBan>> bans;

    @Override
    public void onEnable() {
        loadManagers();
        loadData();
        registerCommands();
        registerCommandCompleter();
        registerEvents();
        getLogger().info("PerWorldPunish v" + getPluginMeta().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        saveData();
        getLogger().info("PerWorldPunish v" + getPluginMeta().getVersion() + " disabled!");
    }

    private void registerCommands() {
        new WorldBanCommand(this, configManager);
        new WorldUnbanCommand(this, configManager);
        new WorldBanListCommand(this, configManager);
        new WorldKickCommand(this, configManager);
        new WorldTempBanCommand(this, configManager);
        new PerWorldPunishCommand(this, configManager);
    }

    private void loadManagers() {
        this.configManager = new ConfigManager(this);
        this.dataManager = new DataManager(this);
        this.bans = new HashMap<>();
    }

    private void registerCommandCompleter() {
        WorldBanCommandCompleter worldBanCommandCompleter = new WorldBanCommandCompleter(this);

        getCommand("worldban").setTabCompleter(worldBanCommandCompleter);
        getCommand("worldkick").setTabCompleter(worldBanCommandCompleter);
        getCommand("worldunban").setTabCompleter(worldBanCommandCompleter);
        getCommand("worldtempban").setTabCompleter(worldBanCommandCompleter);
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new BanListener(this, configManager), this);
    }

    private void loadData() {
        bans = dataManager.loadBans();
    }

    private void saveData() {
        dataManager.saveBans(bans);
    }

    public Map<UUID, Set<WorldBan>> getBans() {
        return bans;
    }

    public void addBan(@NotNull UUID playerId, @NotNull WorldBan ban) {
        bans.computeIfAbsent(playerId, uuid -> new HashSet<>()).add(ban);
    }

    public void removeBan(@NotNull UUID playerId, @NotNull String worldName) {
        if (bans.containsKey(playerId)) {
            bans.get(playerId).removeIf(ban -> ban.getWorld().equalsIgnoreCase(worldName));
            if (bans.get(playerId).isEmpty()) {
                bans.remove(playerId);
            }
        }
    }

    public boolean isBanned(@NotNull UUID playerId, @NotNull String worldName) {
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