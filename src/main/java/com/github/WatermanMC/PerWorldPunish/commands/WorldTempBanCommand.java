package com.github.WatermanMC.PerWorldPunish.commands;

import com.github.WatermanMC.PerWorldPunish.*;
import com.github.WatermanMC.PerWorldPunish.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.World;
import java.util.UUID;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class WorldTempBanCommand implements CommandExecutor {
    private PerWorldPunish plugin;
    private ConfigManager configManager;
    private MiniMessage miniMessage;

    public WorldTempBanCommand(PerWorldPunish plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.miniMessage = MiniMessage.miniMessage();
        plugin.getCommand("worldtempban").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (!sender.hasPermission("perworldpunish.worldtempban")) {
            sender.sendMessage(miniMessage.deserialize(configManager.getMessage("nopermission")));
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(miniMessage.deserialize("<red>Usage: /worldtempban <player> <world> <timeInMinutes> [reason]"));
            return true;
        }

        String playerName = args[0];
        String worldName = args[1];

        try {
            int minutes = Integer.parseInt(args[2]);
            if (minutes <= 0) {
                sender.sendMessage(miniMessage.deserialize(configManager.getMessage("timeNotPositive")));
            }

            StringBuilder reasonBuilder = new StringBuilder();
            for (int i = 3; i < args.length; i++) {
                reasonBuilder.append(args[i]).append(" ");
            }

            String reason = reasonBuilder.toString().trim();
            if (reason.isEmpty()) {
                reason = configManager.getDefaultReason();
            }

            Player target = Bukkit.getPlayer(playerName);
            UUID playerId;

            if (target != null) {
                if (target.hasPermission("perworldpunish.admin")) {
                    sender.sendMessage(miniMessage.deserialize(configManager.getMessage("playerPunishImmune")
                            .replace("{player}", playerName)));
                }
                playerId = target.getUniqueId();
            } else {
                playerId = Bukkit.getOfflinePlayer(playerName).getUniqueId();
            }

            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                sender.sendMessage(miniMessage.deserialize(configManager.getMessage("invalidWorld")));
            }

            long expiryTime = System.currentTimeMillis() + (minutes * 60 * 1000L);
            plugin.addBan(playerId, new WorldBan(worldName, reason, expiryTime, true));

            sender.sendMessage(miniMessage.deserialize(configManager.getMessage("tempBanSuccess")
                    .replace("{player}", playerName)
                    .replace("{world}", worldName)
                    .replace("{time}", String.valueOf(minutes))
                    .replace("{reason}", reason)));

            if (target != null && target.isOnline() && target.getWorld().getName().equalsIgnoreCase(worldName)) {
                target.teleport(Bukkit.getWorld(configManager.getFallbackWorld()).getSpawnLocation());
                String msg = configManager.getMessage("playerTempBanned")
                        .replace("{world}", worldName)
                        .replace("{reason}", reason)
                        .replace("{time}", String.valueOf(minutes));
                target.sendMessage(miniMessage.deserialize(msg));
            }
            return true;

        } catch (NumberFormatException e) {
            sender.sendMessage(miniMessage.deserialize(configManager.getMessage("invalidTimeFormat")));
        }

        return true;
    }
}