package com.github.WatermanMC.PerWorldPunish.commands;

import com.github.WatermanMC.PerWorldPunish.*;
import com.github.WatermanMC.PerWorldPunish.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.World;
import java.util.UUID;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class WorldUnbanCommand implements CommandExecutor {
    private PerWorldPunish plugin;
    private ConfigManager configManager;
    private MiniMessage miniMessage;

    public WorldUnbanCommand(PerWorldPunish plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.miniMessage = MiniMessage.miniMessage();
        plugin.getCommand("worldunban").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (!sender.hasPermission("perworldpunish.worldunban")) {
            sender.sendMessage(miniMessage.deserialize(configManager.getMessage("nopermission")));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(miniMessage.deserialize("<red>Usage: /worldunban <player> <world>"));
            return true;
        }

        String playerName = args[0];
        String worldName = args[1];

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            sender.sendMessage(miniMessage.deserialize(configManager.getMessage("invalidWorld")));
            return true;
        }

        UUID playerId = Bukkit.getOfflinePlayer(playerName).getUniqueId();

        if (!plugin.isBanned(playerId, worldName)) {
            sender.sendMessage(miniMessage.deserialize(configManager.getMessage("playerNotBanned")
                    .replace("{world}", worldName)));
            return true;
        }

        plugin.removeBan(playerId, worldName);

        sender.sendMessage(miniMessage.deserialize(configManager.getMessage("unBanSuccess")
                .replace("{player}", playerName)
                .replace("{world}", worldName)));

        return true;
    }
}