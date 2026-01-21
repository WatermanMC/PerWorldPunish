package com.github.WatermanMC.PerWorldPunish.commands;

import com.github.WatermanMC.PerWorldPunish.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.World;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class WorldKickCommand implements CommandExecutor {
    private PerWorldPunish plugin;
    private MiniMessage miniMessage;

    public WorldKickCommand(PerWorldPunish plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        plugin.getCommand("worldkick").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("perworldpunish.worldkick")) {
            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("nopermission")));
        }

        if (args.length < 2) {
            sender.sendMessage(miniMessage.deserialize("<red>Usage: /worldkick <player> <world> [reason]"));
        }

        String playerName = args[0];
        String worldName = args[1];

        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("invalidPlayer")));
        }

        if (target.hasPermission("perworldpunish.admin")) {
            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("playerPunishImmune")
                    .replace("{player}", playerName)));
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("invalidWorld")));
        }

        StringBuilder reasonBuilder = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            reasonBuilder.append(args[i]).append(" ");
        }

        String reason = reasonBuilder.toString().trim();
        if (reason.isEmpty()) {
            reason = plugin.getConfigManager().getDefaultReason();
        }

        if (target.getWorld().getName().equalsIgnoreCase(worldName)) {
            target.teleport(Bukkit.getWorld(plugin.getConfigManager().getFallbackWorld()).getSpawnLocation());

            String playerMsg = plugin.getConfigManager().getMessage("playerKicked")
                    .replace("{world}", worldName)
                    .replace("{reason}", reason);
            target.sendMessage(miniMessage.deserialize(playerMsg));

            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("kickSuccess")
                    .replace("{player}", playerName)
                    .replace("{world}", worldName)
                    .replace("{reason}", reason)));
        } else {
            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("playerNotInWorld")
                    .replace("{world}", worldName)));
        }

        return true;
    }
}