package com.diademiemi.lineation;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.permissions.Permission;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import com.diademiemi.lineation.command.CommandExec;
import com.diademiemi.lineation.command.CommandTabComplete;
import com.diademiemi.lineation.line.LineListener;
import com.diademiemi.lineation.line.LineIO;

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

        pm.registerEvents(new LineListener(), plugin);

        pm.addPermission(new Permission("lineation.help"));
        pm.addPermission(new Permission("lineation.reload"));
        pm.addPermission(new Permission("lineation.line.help"));
        pm.addPermission(new Permission("lineation.line.list"));
        pm.addPermission(new Permission("lineation.line.create"));
        pm.addPermission(new Permission("lineation.line.remove"));
        pm.addPermission(new Permission("lineation.line.setarea"));
        pm.addPermission(new Permission("lineation.line.addborder"));
        pm.addPermission(new Permission("lineation.line.removeborder"));
        pm.addPermission(new Permission("lineation.line.start"));
        pm.addPermission(new Permission("lineation.line.stop"));
        pm.addPermission(new Permission("lineation.line.option.messagereach"));
        pm.addPermission(new Permission("lineation.line.option.maxwins"));

        getCommand("lineation").setExecutor(new CommandExec());
        getCommand("lineation").setTabCompleter(new CommandTabComplete());

        new BukkitRunnable() {
            @Override
            public void run() {
                LineIO.loadAll();
            }
        }.runTaskLater(plugin, 2);
    }

    /**
     * disable plugin
     */
    @Override
    public void onDisable() {
        LineIO.saveAll();
        plugin = null;
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
