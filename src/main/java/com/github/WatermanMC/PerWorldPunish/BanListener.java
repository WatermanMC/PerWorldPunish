package com.github.WatermanMC.PerWorldPunish;

import com.github.WatermanMC.PerWorldPunish.commands.*;
import com.github.WatermanMC.PerWorldPunish.managers.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import java.util.Set;
import java.util.UUID;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BanListener implements Listener {
    private PerWorldPunish plugin;
    private MiniMessage miniMessage;

    public BanListener(PerWorldPunish plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        checkAndHandleBan(player, worldName);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        checkAndHandleBan(player, worldName);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event, String worldName, Player player) {
        if (plugin.isBanned(event.getPlayer().getUniqueId(), event.getTo().getWorld().getName())) {
            WorldBan ban = getBanForWorld(player.getUniqueId(), worldName);
            if (ban != null) {
                String reason = ban.getReason();
                if (reason.isEmpty()) {
                    reason = plugin.getConfigManager().getDefaultReason();
                }

                Component message;
                if (ban.isTemporary()) {
                    long remainingMinutes = ban.getRemainingTime() / (60 * 1000);
                    String msg = plugin.getConfigManager().getMessage("playerTempBanned")
                            .replace("{world}", worldName)
                            .replace("{reason}", reason)
                            .replace("{time}", String.valueOf(remainingMinutes));
                    message = miniMessage.deserialize(msg);
                } else {
                    String msg = plugin.getConfigManager().getMessage("playerBanned")
                            .replace("{world}", worldName)
                            .replace("{reason}", reason);
                    message = miniMessage.deserialize(msg);
                }
                player.sendMessage(message);
            }
            event.setCancelled(true);
        }
    }

    private void checkAndHandleBan(Player player, String worldName) {
        if (plugin.isBanned(player.getUniqueId(), worldName)) {
            String fallbackWorld = plugin.getConfigManager().getFallbackWorld();

            World fallback = Bukkit.getWorld(fallbackWorld);
            if (fallback == null) {
                fallback = Bukkit.getWorlds().getFirst();
            }

            Location spawn = fallback.getSpawnLocation();
            player.teleport(spawn);

            WorldBan ban = getBanForWorld(player.getUniqueId(), worldName);
            if (ban != null) {
                String reason = ban.getReason();
                if (reason.isEmpty()) {
                    reason = plugin.getConfigManager().getDefaultReason();
                }

                Component message;
                if (ban.isTemporary()) {
                    long remainingMinutes = ban.getRemainingTime() / (60 * 1000);
                    String msg = plugin.getConfigManager().getMessage("playerTempBanned")
                            .replace("{world}", worldName)
                            .replace("{reason}", reason)
                            .replace("{time}", String.valueOf(remainingMinutes));
                    message = miniMessage.deserialize(msg);
                } else {
                    String msg = plugin.getConfigManager().getMessage("playerBanned")
                            .replace("{world}", worldName)
                            .replace("{reason}", reason);
                    message = miniMessage.deserialize(msg);
                }
                player.sendMessage(message);
            }
        }
    }

    private WorldBan getBanForWorld(UUID playerId, String worldName) {
        Set<WorldBan> playerBans = plugin.getBans().get(playerId);
        if (playerBans == null) return null;

        for (WorldBan ban : playerBans) {
            if (ban.getWorld().equalsIgnoreCase(worldName)) {
                return ban;
            }
        }
        return null;
    }
}