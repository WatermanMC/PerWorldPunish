package com.github.WatermanMC.PerWorldPunish.managers;

import com.github.WatermanMC.PerWorldPunish.*;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class DataManager {
    private PerWorldPunish plugin;
    private File dataFile;
    private YamlConfiguration data;

    public DataManager() {
        this.plugin = PerWorldPunish.getInstance();
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        createFile();
    }

    private void createFile() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create bans.yml", e);
                plugin.getLogger().severe("Cant fix it? Join on our fast discord support: https://discord.gg/Scgqfm5EU4");
            }
        }
    }

    public Map<UUID, Set<WorldBan>> loadBans() {
        data = YamlConfiguration.loadConfiguration(dataFile);
        Map<UUID, Set<WorldBan>> bans = new HashMap<>();

        if (!data.contains("bans")) {
            return bans;
        }

        for (String playerIdStr : data.getConfigurationSection("bans").getKeys(false)) {
            UUID playerId = UUID.fromString(playerIdStr);
            Set<WorldBan> playerBans = new HashSet<>();

            for (String world : data.getConfigurationSection("bans." + playerIdStr).getKeys(false)) {
                String path = "bans." + playerIdStr + "." + world;
                String reason = data.getString(path + ".reason");
                long expiryTime = data.getLong(path + ".expiryTime", 0);
                boolean isTemp = data.getBoolean(path + ".isTemporary", false);

                playerBans.add(new WorldBan(world, reason, expiryTime, isTemp));
            }

            bans.put(playerId, playerBans);
        }

        return bans;
    }

    public void saveBans(Map<UUID, Set<WorldBan>> bans) {
        data = new YamlConfiguration();

        for (Map.Entry<UUID, Set<WorldBan>> entry : bans.entrySet()) {
            String playerIdStr = entry.getKey().toString();

            for (WorldBan ban : entry.getValue()) {
                String path = "bans." + playerIdStr + "." + ban.getWorld();
                data.set(path + ".reason", ban.getReason());
                data.set(path + ".expiryTime", ban.getExpiryTime());
                data.set(path + ".isTemporary", ban.isTemporary());
            }
        }

        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save bans.yml", e);
            plugin.getLogger().severe("Cant fix it? Join on our fast discord support: https://discord.gg/Scgqfm5EU4");
        }
    }
}