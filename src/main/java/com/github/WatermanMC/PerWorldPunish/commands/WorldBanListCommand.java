package com.github.WatermanMC.PerWorldPunish.commands;

import com.github.WatermanMC.PerWorldPunish.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.OfflinePlayer;
import java.util.*;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class WorldBanListCommand implements CommandExecutor {
    private PerWorldPunish plugin;
    private MiniMessage miniMessage;

    public WorldBanListCommand(PerWorldPunish plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        plugin.getCommand("worldbanlist").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("perworldpunish.worldbanlist")) {
            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("nopermission")));
        }

        Map<UUID, Set<WorldBan>> bans = plugin.getBans();

        if (bans.isEmpty()) {
            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("noBannedPlayers")));
        }

        sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("banListPrefix")));

        for (Map.Entry<UUID, Set<WorldBan>> entry : bans.entrySet()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(entry.getKey());
            String playerName = player.getName() != null ? player.getName() : entry.getKey().toString();

            for (WorldBan ban : entry.getValue()) {
                String message;
                if (ban.isTemporary()) {
                    long remainingMinutes = ban.getRemainingTime() / (60 * 1000);
                    message = plugin.getConfigManager().getMessage("banListFormat-tempban")
                            .replace("{player}", playerName)
                            .replace("{world}", ban.getWorld())
                            .replace("{time}", String.valueOf(remainingMinutes));
                } else {
                    message = plugin.getConfigManager().getMessage("banListFormat")
                            .replace("{player}", playerName)
                            .replace("{world}", ban.getWorld());
                }
                sender.sendMessage(miniMessage.deserialize(message));
            }
        }

        return true;
    }
}