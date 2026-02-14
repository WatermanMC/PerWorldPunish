package com.github.WatermanMC.PerWorldPunish.commands.tabcompleter;

import com.github.WatermanMC.PerWorldPunish.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WorldBanCommandCompleter implements TabCompleter {

    private final PerWorldPunish plugin;

    public WorldBanCommandCompleter(PerWorldPunish plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command command,
                                      @NotNull String label,
                                      @NotNull String[] args) {

        if (!(sender instanceof Player)) return null;

        if (args.length == 1) {
            return null;
        }

        if (args.length == 2) {
            List<String> worldNames = new ArrayList<>();

            for (World world : Bukkit.getWorlds()) {
                worldNames.add(world.getName());
            }

            return StringUtil.copyPartialMatches(args[0], worldNames, new ArrayList<>());
        }

        return new ArrayList<>();
    }

    public PerWorldPunish getPlugin() {
        return plugin;
    }
}