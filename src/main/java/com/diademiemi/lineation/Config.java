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
     * Config file for plugin global settings
     */
    private static Config plugin = new Config("config.yml");

    /**
     * Config file for lines
     */
    private static Config line = new Config("lines.yml");

    /**
     * Config file containing plugin messages
     */
    private static Config message = new Config("messages.yml");

    /**
     * File used to store player wins in
     */
    private static Config data = new Config("data.yml");

    /**
     * File used for config
     */
    private File configFile;

    /**
     * File used as YAML
     */
    private YamlConfiguration YMLConfig;

    /**
     * Filename
     */
    private final String filename;

    /**
     * Create new config
     *
     * @param filename  Filename to use
     */
    public Config(String filename) {
        this.filename = filename;
        configFile = new File(Lineation.getInstance().getDataFolder(), filename);
    }

    /**
     * Return plugin global config
     *
     * @return  Plugin global config
     */
    public static Config getPluginConfig() {
        return plugin;
    }

    /**
     * Return config for lines
     *
     * @return  Line config
     */
    public static Config getLineConfig() {
        return line;
    }
    
    /**
     * Return config for messages
     *
     * @return  Message config
     */
    public static Config getMessageConfig() {
        return message;
    }

    /**
     * Return data
     *
     * @return Data
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
     * Gets YMLConfig
     * Reloads config if YMLConfig is null
     *
     * @return YML Configuration
     */
    
    public YamlConfiguration getConfig() {
        if (YMLConfig == null) {
            reloadConfig();
        }
        return YMLConfig;
    }

    /**
     * Saves configuration file
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
     * Saves default configuration files from resources directory
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
