package com.diademiemi.lineation;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.permissions.Permission;
import org.bukkit.entity.Entity;

import com.diademiemi.lineation.command.CommandExec;

public class Lineation extends JavaPlugin { 

    /**
     * Plugin instance
     */
    private static Lineation plugin;
    
    /**
     * Run on startup, load files, create permissions and start
     */
    @Override
    public void onEnable() {
    plugin = this;
    
    Config.getDefaultsConfig().saveDefaultConfig();
    Config.getMessageConfig().saveDefaultConfig();
    Config.getLineConfig().saveDefaultConfig();
    Config.getDataConfig().saveDefaultConfig();

    Message.reloadMessages();

    PluginManager pm = getServer().getPluginManager();

    pm.addPermission(new Permission("lineation.help"));

    getCommand("lineation").setExecutor(new CommandExec());

    }
    // On disable
    @Override
    public void onDisable() {
    
    }

    /**
     * Get plugin instance
     *
     * @return plugin instance
     */
    public static Lineation getInstance() {
        return plugin;
    }

}
