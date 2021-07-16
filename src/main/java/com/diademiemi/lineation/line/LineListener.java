package com.diademiemi.lineation.line;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Location;
import org.bukkit.World;

import com.diademiemi.lineation.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * listeners for lines
 * 
 * @author diademiemi
 */
public class LineListener implements Listener {

    /**
     * player move listener
     *
     * @param e player move event
     */
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            for(Map.Entry<String, Line> entry: Line.getStartedLines().entrySet()) {
                if (entry.getValue().getType().equalsIgnoreCase("finish")) {
                    if (entry.getValue().contains(e.getPlayer())) {
                        LineTools.playerFinish(entry.getValue(), e.getPlayer());
                    }
                }
            }
        }
    }    
}

