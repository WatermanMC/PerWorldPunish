package com.github.WatermanMC.PerWorldPunish.managers;

import com.github.WatermanMC.PerWorldPunish.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class ConfigManager {
    private PerWorldPunish plugin;
    private FileConfiguration config;
    private FileConfiguration messages;
    private File configFile;
    private File messagesFile;

    public ConfigManager() {
        this.plugin = PerWorldPunish.getInstance();
        this.createFiles();
        this.reloadConfigs();
    }

    private void createFiles() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }

        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
    }

    public boolean reloadConfigs() {
        String discordSupport = "https://discord.gg/Scgqfm5EU4";
        String currentFile = "config.yml";
        try {
            currentFile = "config.yml";
            config = new YamlConfiguration();
            config.load(configFile);

            currentFile = "messages.yml";
            messages = new YamlConfiguration();
            messages.load(messagesFile);

            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(plugin.getResource("config.yml"), StandardCharsets.UTF_8));
            config.setDefaults(defaultConfig);

            YamlConfiguration defaultMessages = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(plugin.getResource("messages.yml"), StandardCharsets.UTF_8));
            messages.setDefaults(defaultMessages);

            return true;

        } catch (InvalidConfigurationException e) {
            String location = this.extractLocation(e.getMessage());
            plugin.getLogger().warning("Possible fix: The error is " + location + " on file " + currentFile);
            plugin.getLogger().warning("Cant fix it? Join on our fast discord support: " + discordSupport);
            return false;

        } catch (java.io.IOException e) {
            plugin.getLogger().warning("Could not read file:" + currentFile);
            plugin.getLogger().warning("Make sure the file exists and the server has permission to read it.");
            plugin.getLogger().warning("Cant fix it? Join on our fast discord support: " + discordSupport);
            return false;

        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Critical error during config reload.", e);
            plugin.getLogger().severe("This is an internal plugin error! Please join on our discord support: " + discordSupport);
            return false;
        }
    }

    private String extractLocation(String msg) {
        if (msg == null) return "unknown location";
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("line \\d+, column \\d+").matcher(msg);
        if (matcher.find()) {
            return "in " + matcher.group();
        }
        return "a syntax error";
    }

    public String getDefaultReason() {
        return config.getString("default-reason", "<red>No reason provided.");
    }

    public String getFallbackWorld() {
        return config.getString("fallback-world", "world");
    }

    public String getMessage(String path) {
        return messages.getString(path, "<red>Message not found: " + path);
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config.yml", e);
        }
    }

    public void saveMessages() {
        try {
            messages.save(messagesFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save messages.yml", e);
        }
    }
}