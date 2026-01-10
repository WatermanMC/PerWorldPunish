package com.github.WatermanMC.PerWorldPunish.api;

import java.util.Set;
import java.util.UUID;

public interface PerWorldPunishAPI {
    boolean banPlayer(UUID playerId, String worldName, String reason);
    boolean tempBanPlayer(UUID playerId, String worldName, long minutes, String reason);
    boolean unbanPlayer(UUID playerId, String worldName);
    boolean kickPlayer(UUID playerId, String worldName, String reason);
    boolean isBanned(UUID playerId, String worldName);
    boolean isTemporarilyBanned(UUID playerId, String worldName);
    Set<String> getPlayerBannedWorlds(UUID playerId);
    Set<UUID> getAllBannedPlayers();
    String getBanReason(UUID playerId, String worldName);
    long getRemainingBanTime(UUID playerId, String worldName);
    void reloadConfiguration();
    String getDefaultReason();
    String getFallbackWorld();
}