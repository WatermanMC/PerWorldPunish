package com.github.WatermanMC.PerWorldPunish.managers;

import com.github.WatermanMC.PerWorldPunish.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class DataManager {
    private final PerWorldPunish plugin;
    private final File dataFile;
    private boolean isDirty = false;

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
                plugin.getLogger().log(Level.SEVERE, "Could not create data.yml", e);
            }
        }
    }

    public void markDirty() {
        this.isDirty = true;
    }

    public Map<UUID, Set<WorldBan>> loadBans() {
        YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
        Map<UUID, Set<WorldBan>> bans = new HashMap<>();

        if (!data.contains("bans") || data.getConfigurationSection("bans") == null) {
            return bans;
        }

        for (String playerIdStr : data.getConfigurationSection("bans").getKeys(false)) {
            try {
                UUID playerId = UUID.fromString(playerIdStr);
                Set<WorldBan> playerBans = new HashSet<>();
                var worldSection = data.getConfigurationSection("bans." + playerIdStr);

                if (worldSection != null) {
                    for (String world : worldSection.getKeys(false)) {
                        String path = "bans." + playerIdStr + "." + world;
                        String reason = data.getString(path + ".reason", "No reason");
                        long expiryTime = data.getLong(path + ".expiryTime", 0);
                        boolean isTemp = data.getBoolean(path + ".isTemporary", false);

                        if (isTemp && expiryTime > 0 && expiryTime < System.currentTimeMillis()) {
                            markDirty();
                            continue;
                        }

                        playerBans.add(new WorldBan(world, reason, expiryTime, isTemp));
                    }
                }
                if (!playerBans.isEmpty()) {
                    bans.put(playerId, playerBans);
                }
            } catch (IllegalArgumentException ignored) {}
        }
        return bans;
    }

    public boolean saveBans(Map<UUID, Set<WorldBan>> bans, boolean sync) {
        if (!isDirty && !sync) return true;
        String discordSupport = "https://discord.gg/Scgqfm5EU4";

        YamlConfiguration newData = new YamlConfiguration();
        for (Map.Entry<UUID, Set<WorldBan>> entry : bans.entrySet()) {
            String playerIdStr = entry.getKey().toString();
            for (WorldBan ban : entry.getValue()) {
                String path = "bans." + playerIdStr + "." + ban.getWorld();
                newData.set(path + ".reason", ban.getReason());
                newData.set(path + ".expiryTime", ban.getExpiryTime());
                newData.set(path + ".isTemporary", ban.isTemporary());
            }
        }

        if (sync) {
            try {
                newData.save(dataFile);
                return true;
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Manual save failed", e);
                plugin.getLogger().warning("Cant fix it? Join on our fast discord support: " + discordSupport);
                return false;
            }
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    newData.save(dataFile);
                    isDirty = false;
                } catch (IOException e) {
                    plugin.getLogger().log(Level.SEVERE, "Async save failed", e);
                    plugin.getLogger().warning("Cant fix it? Join on our fast discord support: " + discordSupport);
                }
            });
            return true;
        }
    }
}