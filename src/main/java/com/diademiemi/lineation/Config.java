package com.diademiemi.lineation;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

/**
 * Class for managing config files
 *
 * @author diademiemi
 */
public class Config {

    /**
     * config file for defaults
     */
    private static Config defaults = new Config("config.yml");

    /**
     * line config
     */
    private static Config line = new Config("lines.yml");

    /**
     * message config
     */
    private static Config message = new Config("messages.yml");

    /**
     * data store/config
     */
    private static Config data = new Config("data.yml");

    /**
     * file used for config
     */
    private File configFile;

    /**
     * file used as YAML
     */
    private YamlConfiguration YMLConfig;

    /**
     * filename
     */
    private final String filename;

    /**
     * Create new config
     */
    public Config(String filename) {
        this.filename = filename;
        configFile = new File(Lineation.getInstance().getDataFolder(), filename);
    }

    /**
     * Return config
     */
    public static Config getPluginConfig() {
        return defaults;
    }
    /**
     * Return config for lines
     */
    public static Config getLineConfig() {
        return line;
    }
    
    /**
     * Return config for messages
     *
     * @return message config instance
     */
    public static Config getMessageConfig() {
        return message;
    }

    /**
     * Return config for data
     */
    public static Config getData() {
        return data;
    }
    
    /**
     * Load config, reload config and save defaults if missing
     */
    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(Lineation.getInstance().getDataFolder(), filename);
        }

        YMLConfig = YamlConfiguration.loadConfiguration(configFile);

        Reader defConfigStream = new InputStreamReader(
            Lineation.getInstance().getResource(filename), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        YMLConfig.setDefaults(defConfig);
        YMLConfig.options().copyDefaults(true);
        saveConfig();

        if (filename.equals("messages.yml")) Message.reloadMessages();
    }

    /**
     * Reloads config if YMLConfig is null
     *
     * @return YMLConfig YamlConfiguration
     */
    
    public YamlConfiguration getConfig() {
        if (YMLConfig == null) {
            reloadConfig();
        }
        return YMLConfig;
    }

    /**
     * Saves config
     */
    public void saveConfig() {
        if (YMLConfig == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            Lineation.getInstance().getLogger().log(Level.SEVERE, "Config file failed to save!", ex);
        }
    }

    /**
     * Saves default configs
     */
    public void saveDefaultConfig() {
        if (configFile == null) { 
            configFile = new File(Lineation.getInstance().getDataFolder(), filename);
        }
        if (!configFile.exists()) {
            Lineation.getInstance().saveResource(filename, false);
            YMLConfig = YamlConfiguration.loadConfiguration(configFile);
        }
    }
}
