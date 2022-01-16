package me.diademiemi.lineation.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

import me.diademiemi.lineation.Lineation;

/**
 * Listens to move events
 * 
 * @author diademiemi
 */
public class MoveEvents implements Listener {

    /**
     * Register plugin listener
     *
     * @param plugin    Instance of this plugin
     */
    public MoveEvents(Lineation plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Unregister plugin listeners
     *
     * @param plugin    Instance of this plugin
     */
    public static void unregisterPluginEvents(Lineation plugin) {
        HandlerList.unregisterAll(plugin);
    }

    /**
     * Player move listener
     *
     * @param e Player move event
     */
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            Checker.checkPlayer(e.getPlayer());
		}
	}
}