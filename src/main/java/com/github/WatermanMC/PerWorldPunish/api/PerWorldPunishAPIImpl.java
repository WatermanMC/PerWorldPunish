package com.github.WatermanMC.PerWorldPunish.api;

import com.github.WatermanMC.PerWorldPunish.PerWorldPunish;
import com.github.WatermanMC.PerWorldPunish.WorldBan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.*;

public class PerWorldPunishAPIImpl implements PerWorldPunishAPI {
    private final PerWorldPunish plugin;

    public PerWorldPunishAPIImpl(PerWorldPunish plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean banPlayer(UUID playerId, String worldName, String reason) {
        if (Bukkit.getWorld(worldName) == null) return false;

        Player player = Bukkit.getPlayer(playerId);
        if (player != null && player.hasPermission("perworldpunish.admin")) {
            return false;
        }

        if (reason == null || reason.isEmpty()) {
            reason = plugin.getConfigManager().getDefaultReason();
        }

        plugin.addBan(playerId, new WorldBan(worldName, reason));

        if (player != null && player.isOnline() &&
                player.getWorld().getName().equalsIgnoreCase(worldName)) {
            player.teleport(Bukkit.getWorld(plugin.getConfigManager().getFallbackWorld()).getSpawnLocation());
        }

        return true;
    }

    @Override
    public boolean tempBanPlayer(UUID playerId, String worldName, long minutes, String reason) {
        if (Bukkit.getWorld(worldName) == null || minutes <= 0) return false;

        Player player = Bukkit.getPlayer(playerId);
        if (player != null && player.hasPermission("perworldpunish.admin")) {
            return false;
        }

        if (reason == null || reason.isEmpty()) {
            reason = plugin.getConfigManager().getDefaultReason();
        }

        long expiryTime = System.currentTimeMillis() + (minutes * 60 * 1000L);
        plugin.addBan(playerId, new WorldBan(worldName, reason, expiryTime, true));

        if (player != null && player.isOnline() &&
                player.getWorld().getName().equalsIgnoreCase(worldName)) {
            player.teleport(Bukkit.getWorld(plugin.getConfigManager().getFallbackWorld()).getSpawnLocation());
        }

        return true;
    }

    @Override
    public boolean unbanPlayer(UUID playerId, String worldName) {
        if (Bukkit.getWorld(worldName) == null) return false;

        if (!plugin.isBanned(playerId, worldName)) {
            return false;
        }

        plugin.removeBan(playerId, worldName);
        return true;
    }

    @Override
    public boolean kickPlayer(UUID playerId, String worldName, String reason) {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null) return false;

        if (player.hasPermission("perworldpunish.admin")) {
            return false;
        }

        if (!player.getWorld().getName().equalsIgnoreCase(worldName)) {
            return false;
        }

        if (reason == null || reason.isEmpty()) {
            reason = plugin.getConfigManager().getDefaultReason();
        }

        player.teleport(Bukkit.getWorld(plugin.getConfigManager().getFallbackWorld()).getSpawnLocation());
        return true;
    }

    @Override
    public boolean isBanned(UUID playerId, String worldName) {
        return plugin.isBanned(playerId, worldName);
    }

    @Override
    public boolean isTemporarilyBanned(UUID playerId, String worldName) {
        Set<WorldBan> bans = plugin.getBans().getOrDefault(playerId, new HashSet<>());
        for (WorldBan ban : bans) {
            if (ban.getWorld().equalsIgnoreCase(worldName) && ban.isTemporary()) {
                return ban.getRemainingTime() > 0;
            }
        }
        return false;
    }

    @Override
    public Set<String> getPlayerBannedWorlds(UUID playerId) {
        Set<String> bannedWorlds = new HashSet<>();
        Set<WorldBan> bans = plugin.getBans().getOrDefault(playerId, new HashSet<>());

        for (WorldBan ban : bans) {
            if (ban.isTemporary()) {
                if (ban.getRemainingTime() > 0) {
                    bannedWorlds.add(ban.getWorld());
                }
            } else {
                bannedWorlds.add(ban.getWorld());
            }
        }

        return bannedWorlds;
    }

    @Override
    public Set<UUID> getAllBannedPlayers() {
        return plugin.getBans().keySet();
    }

    @Override
    public String getBanReason(UUID playerId, String worldName) {
        Set<WorldBan> bans = plugin.getBans().getOrDefault(playerId, new HashSet<>());
        for (WorldBan ban : bans) {
            if (ban.getWorld().equalsIgnoreCase(worldName)) {
                return ban.getReason();
            }
        }
        return null;
    }

    @Override
    public long getRemainingBanTime(UUID playerId, String worldName) {
        Set<WorldBan> bans = plugin.getBans().getOrDefault(playerId, new HashSet<>());
        for (WorldBan ban : bans) {
            if (ban.getWorld().equalsIgnoreCase(worldName) && ban.isTemporary()) {
                return ban.getRemainingTime();
            }
        }
        return 0;
    }

    @Override
    public void reloadConfiguration() {
        plugin.getConfigManager().reloadConfigs();
    }

    @Override
    public String getDefaultReason() {
        return plugin.getConfigManager().getDefaultReason();
    }

    @Override
    public String getFallbackWorld() {
        return plugin.getConfigManager().getFallbackWorld();
    }
}