package com.github.WatermanMC.PerWorldPunish.commands;

import com.github.WatermanMC.PerWorldPunish.*;
import com.github.WatermanMC.PerWorldPunish.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.World;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class WorldKickCommand implements CommandExecutor {
    private PerWorldPunish plugin;
    private ConfigManager configManager;
    private MiniMessage miniMessage;

    public WorldKickCommand(PerWorldPunish plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.miniMessage = MiniMessage.miniMessage();
        plugin.getCommand("worldkick").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (!sender.hasPermission("perworldpunish.worldkick")) {
            sender.sendMessage(miniMessage.deserialize(configManager.getMessage("nopermission")));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(miniMessage.deserialize("<red>Usage: /worldkick <player> <world> [reason]"));
            return true;
        }

        String playerName = args[0];
        String worldName = args[1];

        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(miniMessage.deserialize(configManager.getMessage("invalidPlayer")));
            return true;
       }

        if (target.hasPermission("perworldpunish.admin")) {
            sender.sendMessage(miniMessage.deserialize(configManager.getMessage("playerPunishImmune")
                    .replace("{player}", playerName)));
            return true;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            sender.sendMessage(miniMessage.deserialize(configManager.getMessage("invalidWorld")));
            return true;
        }

        StringBuilder reasonBuilder = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            reasonBuilder.append(args[i]).append(" ");
        }

        String reason = reasonBuilder.toString().trim();
        if (reason.isEmpty()) {
            reason = configManager.getDefaultReason();
        }

        if (target.getWorld().getName().equalsIgnoreCase(worldName)) {
            target.teleport(Bukkit.getWorld(configManager.getFallbackWorld()).getSpawnLocation());

            String playerMsg = configManager.getMessage("playerKicked")
                    .replace("{world}", worldName)
                    .replace("{reason}", reason);
            target.sendMessage(miniMessage.deserialize(playerMsg));

            sender.sendMessage(miniMessage.deserialize(configManager.getMessage("kickSuccess")
                    .replace("{player}", playerName)
                    .replace("{world}", worldName)
                    .replace("{reason}", reason)));
            return true;
        } else {
            sender.sendMessage(miniMessage.deserialize(configManager.getMessage("playerNotInWorld")
                    .replace("{world}", worldName)));
        }

        return true;
    }
}