package com.github.WatermanMC.PerWorldPunish;

import com.github.WatermanMC.PerWorldPunish.api.*;
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

        // 1. Call the event through the main class and check if cancelled
        PlayerWorldBanEvent event = plugin.callBanEvent(playerId, worldName, reason);
        if (event.isCancelled()) return false;

        Player player = Bukkit.getPlayer(playerId);
        if (player != null && player.hasPermission("perworldpunish.admin")) return false;

        String finalReason = (reason == null || reason.isEmpty()) ? plugin.getConfigManager().getDefaultReason() : reason;
        plugin.addBan(playerId, new WorldBan(worldName, finalReason));

        handleTeleport(player, worldName);
        return true;
    }

    @Override
    public boolean tempBanPlayer(UUID playerId, String worldName, long minutes, String reason) {
        if (Bukkit.getWorld(worldName) == null || minutes <= 0) return false;

        // 1. Call the event through the main class
        PlayerWorldTempBanEvent event = plugin.callTempBanEvent(playerId, worldName, minutes, reason);
        if (event.isCancelled()) return false;

        Player player = Bukkit.getPlayer(playerId);
        if (player != null && player.hasPermission("perworldpunish.admin")) return false;

        String finalReason = (reason == null || reason.isEmpty()) ? plugin.getConfigManager().getDefaultReason() : reason;
        long expiryTime = System.currentTimeMillis() + (minutes * 60 * 1000L);
        plugin.addBan(playerId, new WorldBan(worldName, finalReason, expiryTime, true));

        handleTeleport(player, worldName);
        return true;
    }

    @Override
    public boolean unbanPlayer(UUID playerId, String worldName) {
        if (!plugin.isBanned(playerId, worldName)) return false;

        // 1. Call the event through the main class
        plugin.callUnbanEvent(playerId, worldName);

        plugin.removeBan(playerId, worldName);
        return true;
    }

    @Override
    public boolean kickPlayer(UUID playerId, String worldName, String reason) {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null || !player.getWorld().getName().equalsIgnoreCase(worldName)) return false;

        // 1. Call the event through the main class
        PlayerWorldKickEvent event = plugin.callKickEvent(playerId, worldName, reason);
        if (event.isCancelled()) return false;

        if (player.hasPermission("perworldpunish.admin")) return false;

        handleTeleport(player, worldName);
        return true;
    }

    private void handleTeleport(Player player, String worldName) {
        if (player != null && player.isOnline() && player.getWorld().getName().equalsIgnoreCase(worldName)) {
            player.teleport(Bukkit.getWorld(plugin.getConfigManager().getFallbackWorld()).getSpawnLocation());
        }
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