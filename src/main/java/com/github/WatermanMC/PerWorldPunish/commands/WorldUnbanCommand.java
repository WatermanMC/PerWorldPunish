package com.github.WatermanMC.PerWorldPunish.commands;

import com.github.WatermanMC.PerWorldPunish.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.World;
import java.util.UUID;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class WorldUnbanCommand implements CommandExecutor {
    private PerWorldPunish plugin;
    private MiniMessage miniMessage;

    public WorldUnbanCommand(PerWorldPunish plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        plugin.getCommand("worldunban").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("perworldpunish.worldunban")) {
            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("nopermission")));
        }

        if (args.length < 2) {
            sender.sendMessage(miniMessage.deserialize("<red>Usage: /worldunban <player> <world>"));
        }

        String playerName = args[0];
        String worldName = args[1];

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("invalidWorld")));
        }

        UUID playerId = Bukkit.getOfflinePlayer(playerName).getUniqueId();

        if (!plugin.isBanned(playerId, worldName)) {
            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("playerNotBanned")
                    .replace("{world}", worldName)));
        }

        plugin.removeBan(playerId, worldName);

        sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("unBanSuccess")
                .replace("{player}", playerName)
                .replace("{world}", worldName)));

        return true;
    }
}