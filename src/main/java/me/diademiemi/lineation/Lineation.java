package me.diademiemi.lineation;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.permissions.Permission;
import org.bukkit.scheduler.BukkitRunnable;

import me.diademiemi.lineation.command.CommandExec;
import me.diademiemi.lineation.command.CommandTabComplete;
import me.diademiemi.lineation.line.LineIO;

/**
 * Lineation plugin
 *
 * @author diademiemi
 */
public class Lineation extends JavaPlugin { 

    /**
     * Plugin instance
     */
    private static Lineation plugin;

    /**
     * Plugin manager
     */
    private static PluginManager pm;


    /**
     * Run on startup, load files, create permissions and start
     */
    @Override
    public void onEnable() {
        plugin = this;
        
        Config.getPluginConfig().saveDefaultConfig();
        Config.getMessageConfig().saveDefaultConfig();
        Config.getLineConfig().saveDefaultConfig();
        Config.getData().saveDefaultConfig();

        Message.reloadMessages();

        pm = getServer().getPluginManager();

        pm.addPermission(new Permission("lineation.help"));
        pm.addPermission(new Permission("lineation.reload"));
        pm.addPermission(new Permission("lineation.maxwins"));
        pm.addPermission(new Permission("lineation.forget"));
        pm.addPermission(new Permission("lineation.line.list"));
        pm.addPermission(new Permission("lineation.line.create"));
        pm.addPermission(new Permission("lineation.line.remove"));
        pm.addPermission(new Permission("lineation.line.setarea"));
        pm.addPermission(new Permission("lineation.line.addborder"));
        pm.addPermission(new Permission("lineation.line.removeborder"));
        pm.addPermission(new Permission("lineation.line.addcheckpoint"));
        pm.addPermission(new Permission("lineation.line.removecheckpoint"));
        pm.addPermission(new Permission("lineation.line.start"));
        pm.addPermission(new Permission("lineation.line.stop"));
        pm.addPermission(new Permission("lineation.line.option.messagereach"));
        pm.addPermission(new Permission("lineation.line.option.maxwinners"));
        pm.addPermission(new Permission("lineation.line.option.addcommand"));
        pm.addPermission(new Permission("lineation.line.option.removecommand"));
        pm.addPermission(new Permission("lineation.line.option.gamemodes"));
        pm.addPermission(new Permission("lineation.line.option.blocksequence"));
        pm.addPermission(new Permission("lineation.line.option.teleport"));
        pm.addPermission(new Permission("lineation.line.option.laps"));
        pm.addPermission(new Permission("lineation.line.option.link"));
		pm.addPermission(new Permission("lineation.line.option.illegalarea"));
		pm.addPermission(new Permission("lineation.line.option.illegalarea.add"));
		pm.addPermission(new Permission("lineation.line.option.illegalarea.remove"));
		pm.addPermission(new Permission("lineation.line.option.illegalarea.gamemodes"));

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
     * Disable plugin
     */
    @Override
    public void onDisable() {
        LineIO.saveAll();
        Config.getPluginConfig().saveConfig();
        Config.getData().saveConfig();
        plugin = null;
    }

    /**
     * Get plugin instance
     *
     * @return Plugin instance
     */
    public static Lineation getInstance() {
        return plugin;
    }

}
