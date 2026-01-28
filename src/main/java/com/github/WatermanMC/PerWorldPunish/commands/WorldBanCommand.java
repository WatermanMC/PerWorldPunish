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

public class WorldBanCommand implements CommandExecutor {
    private PerWorldPunish plugin;
    private MiniMessage miniMessage;

    public WorldBanCommand(PerWorldPunish plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        plugin.getCommand("worldban").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("perworldpunish.worldban")) {
            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("nopermission")));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(miniMessage.deserialize("<red>Usage: /worldban <player> <world> [reason]"));
            return true;
        }

        String playerName = args[0];
        String worldName = args[1];
        StringBuilder reasonBuilder = new StringBuilder();

        for (int i = 2; i < args.length; i++) {
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
                return true;
            }
            playerId = target.getUniqueId();
        } else {
            playerId = Bukkit.getOfflinePlayer(playerName).getUniqueId();
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("invalidWorld")));
            return true;
        }

        plugin.addBan(playerId, new WorldBan(worldName, reason));

        sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("banSuccess")
                .replace("{player}", playerName)
                .replace("{world}", worldName)
                .replace("{reason}", reason)));

        if (target != null && target.isOnline() && target.getWorld().getName().equalsIgnoreCase(worldName)) {
            target.teleport(Bukkit.getWorld(plugin.getConfigManager().getFallbackWorld()).getSpawnLocation());
            String msg = plugin.getConfigManager().getMessage("playerBanned")
                    .replace("{world}", worldName)
                    .replace("{reason}", reason);
            target.sendMessage(miniMessage.deserialize(msg));
            return true;
        }

        return true;
    }
}