package com.github.WatermanMC.PerWorldPunish.commands;

import com.github.WatermanMC.PerWorldPunish.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.World;
import java.util.UUID;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class WorldTempBanCommand implements CommandExecutor {
    private PerWorldPunish plugin;
    private MiniMessage miniMessage;

    public WorldTempBanCommand(PerWorldPunish plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        plugin.getCommand("worldtempban").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("perworldpunish.worldtempban")) {
            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("nopermission")));
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
                sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("timeNotPositive")));
            }

            StringBuilder reasonBuilder = new StringBuilder();
            for (int i = 3; i < args.length; i++) {
                reasonBuilder.append(args[i]).append(" ");
            }

            String reason = reasonBuilder.toString().trim();
            if (reason.isEmpty()) {
                reason = plugin.getConfigManager().getDefaultReason();
            }

            Player target = Bukkit.getPlayer(playerName);
            UUID playerId;

            if (target != null) {
                if (target.hasPermission("perworldpunish.admin")) {
                    sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("playerPunishImmune")
                            .replace("{player}", playerName)));
                }
                playerId = target.getUniqueId();
            } else {
                playerId = Bukkit.getOfflinePlayer(playerName).getUniqueId();
            }

            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("invalidWorld")));
            }

            long expiryTime = System.currentTimeMillis() + (minutes * 60 * 1000L);
            plugin.addBan(playerId, new WorldBan(worldName, reason, expiryTime, true));

            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("tempBanSuccess")
                    .replace("{player}", playerName)
                    .replace("{world}", worldName)
                    .replace("{time}", String.valueOf(minutes))
                    .replace("{reason}", reason)));

            if (target != null && target.isOnline() && target.getWorld().getName().equalsIgnoreCase(worldName)) {
                target.teleport(Bukkit.getWorld(plugin.getConfigManager().getFallbackWorld()).getSpawnLocation());
                String msg = plugin.getConfigManager().getMessage("playerTempBanned")
                        .replace("{world}", worldName)
                        .replace("{reason}", reason)
                        .replace("{time}", String.valueOf(minutes));
                target.sendMessage(miniMessage.deserialize(msg));
            }
            return true;

        } catch (NumberFormatException e) {
            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("invalidTimeFormat")));
        }

        return true;
    }
}