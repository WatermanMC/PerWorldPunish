package com.github.WatermanMC.PerWorldPunish.commands;

import com.github.WatermanMC.PerWorldPunish.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class PerWorldPunishCommand implements CommandExecutor {
    private PerWorldPunish plugin;
    private MiniMessage miniMessage;

    public PerWorldPunishCommand(PerWorldPunish plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        plugin.getCommand("perworldpunish").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(miniMessage.deserialize("<red>Usage: /perworldpunish <info|reload>"));
            return true;
        }

        if (!sender.hasPermission("perworldpunish.admin")) {
            sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("nopermission")));
            return true;
       }

        switch (args[0].toLowerCase()) {
            case "reload" -> {
                boolean success = plugin.getConfigManager().reloadConfigs();

                if (success) {
                    sender.sendMessage(miniMessage.deserialize(plugin.getConfigManager().getMessage("pluginReloaded")));
                } else {
                    sender.sendMessage(miniMessage.deserialize("<red>Plugin reload failed. Please check your console for errors."));
                }
                return true;
            }
            case "info" -> {
                PluginMeta meta = plugin.getPluginMeta();
                sender.sendMessage(miniMessage.deserialize("<aqua><bold>PerWorldPunish <reset><white>v" + meta.getVersion()));
                sender.sendMessage(miniMessage.deserialize("<gray>Allows per-world punishments such as ban, kick and tempban!"));
                sender.sendMessage(miniMessage.deserialize("<gold>Authors<gray>: <reset>" + meta.getAuthors()));
                sender.sendMessage(miniMessage.deserialize("<green>Commands<gray>: <white>/perworldpunish /worldban /worldtempban /worldunban /worldkick /worldbanlist"));
                return true;
            }
            default -> {
                sender.sendMessage(miniMessage.deserialize("<red>Usage: /perworldpunish <info|reload>"));
                return true;
            }
        }
    }
}