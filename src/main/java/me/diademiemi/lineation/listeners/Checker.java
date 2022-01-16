package me.diademiemi.lineation.listeners;

import org.bukkit.entity.Player;

import me.diademiemi.lineation.line.Line;
import me.diademiemi.lineation.line.LineTools;

import java.util.Map;

public class Checker {
    /**
     * Check if the player is in a line
     * 
     * @param p     Player to check
     */
    public static void checkPlayer(Player p) {
        for(Map.Entry<String, Line> entry: Line.getStartedFinishLines().entrySet()) {
            if (entry.getValue().getGameModes().contains(p.getGameMode())) {
                if (entry.getValue().contains(p)) {
                    LineTools.playerFinish(entry.getValue(), p);
                } else {
                    int i = entry.getValue().checkpointsContain(p);
                    if (i != 0) {
                        if (i - 1 <= entry.getValue().getPlayerCheckpoint(p)) {
                            entry.getValue().addPlayerCheckpoint(p, i);
                        }
                    }
                }
            }
        }

        for(Map.Entry<String, Line> entry: Line.getStartedStartLinesIA().entrySet()) {
            if (entry.getValue().getGameModes().contains(p.getGameMode())) {
                if (entry.getValue().illegalAreaContains(p)) {
                    p.teleport(entry.getValue().getTeleportLocation());
                }
            }
        }
    }


}
