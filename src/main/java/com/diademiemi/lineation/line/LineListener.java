package com.diademiemi.lineation.line;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Location;
import org.bukkit.World;

import com.diademiemi.lineation.Message;
import com.diademiemi.lineation.Lineation;

import java.util.HashMap;
import java.util.Map;

/**
 * Listeners for lines
 * 
 * @author diademiemi
 */
public class LineListener implements Listener {

    /**
     * Register plugin listener
     *
     * @param plugin    Instance of this plugin
     */
    public LineListener(Lineation plugin) {
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
            for(Map.Entry<String, Line> entry: Line.getStartedFinishLines().entrySet()) {
                if (entry.getValue().contains(e.getPlayer())) {
                    LineTools.playerFinish(entry.getValue(), e.getPlayer());
                }
            }
        }
    } 
}

