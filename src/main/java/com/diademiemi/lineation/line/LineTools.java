package com.diademiemi.lineation.line;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldedit.function.block.BlockReplace;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.MaxChangedBlocksException;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;

import com.diademiemi.lineation.Lineation;
import com.diademiemi.lineation.Message;
import com.diademiemi.lineation.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * line tools
 *
 * @author diademiemi
 */
public class LineTools {

    private static final Config data = Config.getData();

    /**
     * handle finish player entry
     * @param line Line
     * @param player Player
     */
    public static void playerFinish(Line line, Player player) {
        if (!line.isWinner(player) && line.getGameModes().contains(player.getGameMode())) {
            String uuid = player.getUniqueId().toString();
            if (!data.getConfig().isInt(uuid) ||
                data.getConfig().getInt(uuid) != Config.getPluginConfig().getConfig().getInt("maxwins")) {

                line.addWinner(player);
                finishMessage(line, player, line.getWinners().size());

                if (line.isTeleportEnabled()) {
                    player.teleport(line.getTeleportLocation());
                }

                data.getConfig().set(uuid, data.getConfig().getInt(uuid) + 1);

                if (line.getWinners().size() == line.getMaxWinners()) {
                    LineTools.stopLine(line);
                }

            }
        }
    }
  
    public static void finishMessage(Line line, Player player, Integer place) {
        switch (line.getMessageReach()) {
            case "world":
                List<Player> players = line.getWorld().getPlayers();
                for (Player p : players) {
                    p.sendMessage(Message.PLAYER_FINISHED
                            .replace("$NAME$", player.getName())
                            .replace("$PLACE$", Message.ordinal(place)));
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
        StringBuilder bordersString = new StringBuilder("");
        ArrayList<double[][]> borders = line.getBorders();
        int i = 1;
        for (double[][] b : borders) {
            if (i> 1) {
                bordersString.append("\n");
            }
            bordersString.append(i);
            bordersString.append(": " + "(" + b[0][0] + "," + b[0][1] + "," + b[0][2] + ") -> (" +
                    b[1][0] + "," + b[1][1] + "," + b[1][2] + ")");
            i++;
        }
        player.sendMessage(Message.LINE_INFO
                .replace("$NAME$", line.getName())
                .replace("$STARTED$", Boolean.toString(line.isStarted()))
                .replace("$TYPE$", line.getType())
                .replace("$WORLD$", line.getWorld().getName())
                .replace("$AREA$",
                    "(" + area[0][0] + "," + area[0][1] + "," + area[0][2] + ") -> (" +
                    area[1][0] + "," + area[1][1] + "," + area[1][2] + ")")
                .replace("$BORDERS$", bordersString)
                .replace(".0", ""));
                    

    }

    /**
     * get line options info
     *
     * @param line Line
     * @param player Player
     */
    public static void getLineOptions(Line line, Player player) {
        switch (line.getType()) {
            case "start":
                player.sendMessage(Message.LINE_OPTIONS_START
                        .replace("$NAME$", line.getName())
                        .replace("$BLOCKSEQUENCE$", line.getBlockSequenceString())
                        .replace("$LINKED$", line.getLinkedLine()));
                break;
            case "finish":
                player.sendMessage(Message.LINE_OPTIONS_FINISH
                        .replace("$NAME$", line.getName())
                        .replace("$BLOCKSEQUENCE$", line.getBlockSequenceString())
                        .replace("$TELEPORTLOCATION$", line.getTeleportLocation().toString())
                        .replace("$ALLOWEDWINNERS$", Integer.toString(line.getMaxWinners()))
                        .replace("$MESSAGEREACH$", line.getMessageReach())
                        .replace("$GAMEMODES$", line.getGameModesString())
                        .replace("$LINKED$", line.getLinkedLine()));
                break;
        }           

    }

    /**
     * get winners
     * 
     * @param line Line
     * @param player Player
     */
    public static void getWinnersString(Line line, Player player) {
        StringBuilder winnersString = new StringBuilder("");
        @SuppressWarnings("unchecked")
        ArrayList<String> winners = line.getWinners();
        int i = 1;
        for (String p : winners) {
            if (i > 1) {
                winnersString.append(", ");
            }
            winnersString.append(Message.ordinal(i) + ": ");
            winnersString.append(p);
            i++;
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
        line.setStarted();
        World world = line.getWorld();
        ArrayList<double[][]> borders = line.getBorders();
        ArrayList<String> blockSequence = line.getBlockSequence();
        ArrayList<Player> players = line.getPlayers();

        try (EditSession es = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {

            startLineSequence(es, borders, world, players, blockSequence, 1);

        }

    }

    /**
     * starts stop line
     *
     * @param line line
     */
    public static void startFinishLine(Line line) {
        line.clearWinners();
        line.setStarted();
        World world = line.getWorld();
        ArrayList<double[][]> borders = line.getBorders();
        
        String blockFrom = line.getBlockSequence().get(0);
        
        try (EditSession es = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {

            for (double[][] b : borders) {
                replaceBlocks(es, b, world, "air", blockFrom);
            }

        }

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
                if (line.getLinkedLine() != "") {
                    startFinishLine(Line.getLines().get(line.getLinkedLine()));
                }
                break;
            case "finish":
                startFinishLine(line);
                if (line.getLinkedLine() != "") {
                    startStartLine(Line.getLines().get(line.getLinkedLine()));
                }
                break;
            }
    }

    /**
     * stops start line
     *
     * @param line line
     */
    public static void stopStartLine(Line line) {
        line.setStopped();
        World world = line.getWorld();
        ArrayList<double[][]> borders = line.getBorders();
        String blockTo = line.getBlockSequence().get(0);

        try (EditSession es = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {

            for (double[][] b : borders) {
                replaceBlocks(es, b, world, blockTo, "air");
            }

        }

    }

    /** 
     * stops finish line
     *
     * @param line line
     */
    public static void stopFinishLine(Line line) {
        line.setStopped();
        World world = line.getWorld();
        ArrayList<double[][]> borders = line.getBorders();
        String blockTo = line.getBlockSequence().get(0);

        try (EditSession es = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {

            for (double[][] b : borders) {
                replaceBlocks(es, b, world, blockTo, "air");
            }

        }

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
                if (line.getLinkedLine() != "") {
                    stopFinishLine(Line.getLines().get(line.getLinkedLine()));
                }
                break;
            case "finish":
                stopFinishLine(line);
                if (line.getLinkedLine() != "") {
                    stopStartLine(Line.getLines().get(line.getLinkedLine()));
                }
                break;
        }
    }

    public static void replaceBlocks(EditSession es, double[][] b, World world, String blockTo, String blockFrom) {
        BlockVector3 min = BlockVector3.at(b[0][0], b[0][1], b[0][2]);
        BlockVector3 max = BlockVector3.at(b[1][0], b[1][1], b[1][2]);

        BlockTypeMask mask = new BlockTypeMask(BukkitAdapter.adapt(world), BlockTypes.get(blockFrom));

        CuboidRegion region = new CuboidRegion(BukkitAdapter.adapt(world), min, max); 

        try {
            es.replaceBlocks(region, mask, BlockTypes.get(blockTo).getDefaultState().toBaseBlock());

        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }

    }

    public static void startLineSequence(EditSession es, ArrayList<double[][]> borders, World world, ArrayList<Player> players, ArrayList<String> blockSequence, Integer i) {
        Bukkit.getServer().getScheduler().runTaskLater(Lineation.getInstance(), new Runnable(){
            public void run() {
                if (blockSequence.size() == i) {
                    for (double[][] b : borders) {
                        replaceBlocks(es, b, world, "air", blockSequence.get(i - 1));
                    }

                    for (Player p : players) {
                        p.sendMessage(Message.STARTING_NOW);
                    }

                } else {

                    for (double[][] b : borders) {
                        replaceBlocks(es, b, world, blockSequence.get(i), blockSequence.get(i - 1));
                    }

                    for (Player p : players) {
                        p.sendMessage(Message.STARTING_IN.replace("$SECONDS$", String.valueOf(blockSequence.size() - i)));
                    }

                }

                if (blockSequence.size() != i) {
                    startLineSequence(es, borders, world, players, blockSequence, i + 1);
                }

            }

        },20L);
   
    }

    






}
