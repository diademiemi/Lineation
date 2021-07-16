package com.diademiemi.lineation.line;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;

import com.diademiemi.lineation.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * line tools
 *
 * @author diademiemi
 */
public class LineTools {

    /**
     * handle finish player entry
     * @param line Line
     * @param player Player
     */
    public static void playerFinish(Line line, Player player) {
        if (!line.isWinner(player)) {
            line.addWinner(player);
            finishMessage(line, player, line.getWinners().size());
        }
    }
  
    public static void finishMessage(Line line, Player player, Integer place) {
        switch (line.getMessageReach()) {
            case "world":
                List<Player> players = line.getWorld().getPlayers();
                for (Player p : players) {
                    p.sendMessage(Message.PLAYER_FINISHED
                            .replace("$NAME$", player.getName())
                            .replace("$PlACE$", Message.ordinal(place)));
                }
                break;
            case "all":
                Bukkit.broadcastMessage(Message.PLAYER_FINISHED
                        .replace("$NAME$", player.getName())
                        .replace("$PLACE$", Message.ordinal(place)));
                break;
        }
    }

    /**
     * get line info
     *
     * @param line Line
     * @param player Player
     */
    public static void getLineInfo(Line line, Player player) {
        double[][] area = line.getArea();
        player.sendMessage(Message.LINE_INFO
                .replace("$NAME$", line.getName())
                .replace("$STARTED$", Boolean.toString(line.isStarted()))
                .replace("$TYPE$", line.getType())
                .replace("$WORLD$", line.getWorld().getName())
                .replace("$AREA$",
                    "(" + area[0][0] + "," + area[0][1] + "," + area[0][2] + ") -> (" +
                    area[1][0] + "," + area[1][1] + "," + area[1][2] + ")"));
    }
    
    public static void getWinnersString(Line line, Player player) {
        StringBuilder winnersString = new StringBuilder("");
        ArrayList<Player> winners = line.getWinners();
        for (Player p : winners) {
            winnersString.append(p.getName());
            winnersString.append(", ");
        }
        player.sendMessage(Message.LINE_WINNERS
                .replace("$NAMES$", winnersString)); 
    }

    /*
     *
     * starts start line
     *
     * @param line line
     */
    public static void startStartLine(Line line) {
        line.setStarted(true);
    }

    /**
     * starts stop line
     *
     * @param line line
     */
    public static void startFinishLine(Line line) {
        line.clearWinners();
        line.setStarted(true);
    }

    /**
     * line start method
     *
     * @param line line
     */
    public static void startLine(Line line) {
        switch (line.getType()) {
            case "start":
                startStartLine(line);
                break;
            case "finish":
                startFinishLine(line);
                break;
            }
    }

    /**
     * stops start line
     *
     * @param line line
     */
    public static void stopStartLine(Line line) {
        line.setStarted(false);
    }

    /** 
     * stops finish line
     *
     * @param line line
     */
    public static void stopFinishLine(Line line) {
        line.setStarted(false);
    }

    /**
     * line stop method
     *
     * @param line line
     */
    public static void stopLine(Line line) {
        switch (line.getType()) {
            case "start":
                stopStartLine(line);
                break;
            case "finish":
                stopFinishLine(line);
                break;
        }
    }
}
