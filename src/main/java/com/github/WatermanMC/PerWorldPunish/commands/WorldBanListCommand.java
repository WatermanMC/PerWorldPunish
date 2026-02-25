package com.github.WatermanMC.PerWorldPunish.commands;

import com.github.WatermanMC.PerWorldPunish.*;
import com.github.WatermanMC.PerWorldPunish.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.OfflinePlayer;
import java.util.*;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class WorldBanListCommand implements CommandExecutor {
    private PerWorldPunish plugin;
    private ConfigManager configManager;
    private MiniMessage miniMessage;

    public WorldBanListCommand(PerWorldPunish plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.miniMessage = MiniMessage.miniMessage();
        plugin.getCommand("worldbanlist").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (!sender.hasPermission("perworldpunish.worldbanlist")) {
            sender.sendMessage(miniMessage.deserialize(configManager.getMessage("nopermission")));
            return true;
        }

        Map<UUID, Set<WorldBan>> bans = plugin.getBans();

        if (bans.isEmpty()) {
            sender.sendMessage(miniMessage.deserialize(configManager.getMessage("noBannedPlayers")));
            return true;
        }

        sender.sendMessage(miniMessage.deserialize(configManager.getMessage("banListPrefix")));

        for (Map.Entry<UUID, Set<WorldBan>> entry : bans.entrySet()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(entry.getKey());
            String playerName = player.getName() != null ? player.getName() : entry.getKey().toString();

            for (WorldBan ban : entry.getValue()) {
                String message;
                if (ban.isTemporary()) {
                    long remainingMinutes = ban.getRemainingTime() / (60 * 1000);
                    message = configManager.getMessage("banListFormat-tempban")
                            .replace("{player}", playerName)
                            .replace("{world}", ban.getWorld())
                            .replace("{time}", String.valueOf(remainingMinutes));
                } else {
                    message = configManager.getMessage("banListFormat")
                            .replace("{player}", playerName)
                            .replace("{world}", ban.getWorld());
                }
                sender.sendMessage(miniMessage.deserialize(message));
                return true;
            }
        }

        return true;
    }
}