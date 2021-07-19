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
 * listeners for lines
 * 
 * @author diademiemi
 */
public class LineListener implements Listener {

    /**
     * register plugin events
     */
    public LineListener(Lineation plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * unregister plugin events
     */
    public static void unregisterPluginEvents(Lineation plugin) {
        HandlerList.unregisterAll(plugin);
    }
    /**
     * player move listener
     *
     * @param e player move event
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

