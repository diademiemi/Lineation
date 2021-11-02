package me.diademiemi.lineation.line;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

import me.diademiemi.lineation.Lineation;

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
             	if (entry.getValue().getGameModes().contains(e.getPlayer().getGameMode())) {
					if (entry.getValue().contains(e.getPlayer())) {
						LineTools.playerFinish(entry.getValue(), e.getPlayer());
					} else {
						int i = entry.getValue().checkpointsContain(e.getPlayer());
						if (i != 0) {
							if (i - 1 <= entry.getValue().getPlayerCheckpoint(e.getPlayer())) {
								entry.getValue().addPlayerCheckpoint(e.getPlayer(), i);
							}
						}
					}
				}
			}

			for(Map.Entry<String, Line> entry: Line.getStartedStartLinesIA().entrySet()) {
				if (entry.getValue().getGameModes().contains(e.getPlayer().getGameMode())) {
					if (entry.getValue().illegalAreaContains(e.getPlayer())) {
						e.getPlayer().teleport(entry.getValue().getTeleportLocation());
					}
				}
			}
		}
	}
}