package com.diademiemi.lineation.line;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.diademiemi.lineation.Message;
import com.diademiemi.lineation.Lineation;
import com.diademiemi.lineation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

    /**
     * lineation line handler
     *
     * @author diademiemi
     */

public class Line {
    /** 
     * hashmap of all lines
     */
    private static HashMap<String, Line> lines = new HashMap<>();

    /**
     * hashmap of started lines
     */
    private static HashMap<String, Line> startedLines = new HashMap<>();

    /**
     * hashmap of all started finish lines
     */
    private static HashMap<String, Line> startedFinishLines = new HashMap<>();

    /**
     * line name
     */
    private String name;

    /**
     * started yes or no
     */
    private boolean started;

    /**
     * line area
     */
    private double[][] area;

    /**
     * line lines
     */
    private ArrayList<double[][]> borders;

    /**
     * list of blocks for opening sequence or to close
     */
    private ArrayList<String> blockSequence;

    /**
     * world
     */
    private World world;

    /** 
     * line type (start/finish)
     */
    private String type;

    /**
     * message reach
     */
    private String messageReach;

    /**
     * linked line
     */
    private String linkedLine;

    /**
     * winners (only for finish type)
     */
    private ArrayList<String> winners;

    /**
     * max winners (only for finish type)
     */
    private Integer maxWinners;

    /**
     * allowed game modes (only for finish type)
     */
    private ArrayList<GameMode> allowedGameModes;

    /**
     * teleport enabled
     */
    private boolean teleportEnabled;

    /**
     * teleport location
     */
    private Location teleportLocation;

    /**
     * create line with defaults and provided name
     *
     * @param name line name
     */
    public Line(String name, String type) {
        this.name = name;
        this.type = type;
        started = false;
        linkedLine = "";

        world = Lineation.getInstance().getServer().getWorlds().get(0); 
        messageReach = Config.getPluginConfig().getConfig().getString("linedefaults.option.messagereach");

        area = new double[2][3];
        area[0][0] = 0;
        area[0][1] = 0;
        area[0][2] = 0;
        area[1][0] = 0;
        area[1][1] = 0;
        area[1][2] = 0;

        borders = new ArrayList<double[][]>();
        
        this.setBlockSequence(Config.getPluginConfig().getConfig().getString("linedefaults.option.blocksequence"));

        if (type.equalsIgnoreCase("finish")) {
            winners = new ArrayList<String>();
            maxWinners = Config.getPluginConfig().getConfig().getInt("linedefaults.option.maxwinners");
            this.setGameModes(Config.getPluginConfig().getConfig().getString("linedefaults.option.gamemodes"));
            teleportLocation = new Location(world, 0, 0, 0, 0, 0);
        }

        lines.put(name, this);
    }
        /**
         * get all loaded lines
         *
         * @return hashmap of loaded lines
         */
        public static HashMap<String, Line> getLines() {
            return lines;
        }

        public static HashMap<String, Line> getStartLines() {
            HashMap<String, Line> startLines = new HashMap<String, Line>();
            for(Map.Entry<String, Line> entry: lines.entrySet()) {
                if (entry.getValue().getType().equalsIgnoreCase("start")) {
                    startLines.put(entry.getKey(), entry.getValue());
                }
            }
            return startLines;
        }

        public static HashMap<String, Line> getFinishLines() {
            HashMap<String, Line> finishLines = new HashMap<String, Line>();
            for(Map.Entry<String, Line> entry: lines.entrySet()) {
                if (entry.getValue().getType().equalsIgnoreCase("finish")) {
                    finishLines.put(entry.getKey(), entry.getValue());
                }
            }
            return finishLines;
        }

        public static HashMap<String, Line> getStartedLines() {
            return startedLines;
        }

        public static HashMap<String, Line> getStartedFinishLines() {
            return startedFinishLines;
        }


        /**
         * get line name
         *
         * @return name of line
         */
        public String getName() {
            return name;
        }

        /** 
         * names/renames this line
         *
         * @param name new name
         */
        public void setName(String name) {
            lines.remove(this.name);
            this.name = name;
            lines.put(name, this);
        }

        /**
        * get line type
         *
         * @return line type
         */
        public String getType() {
            return type;
        }

        /**
         * set line type
         *
         * @param string type
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * get message reach
         */
        public String getMessageReach() {
            return messageReach;
        }
        
        /**
         * set message reach
         *
         * @param string messageReach
         */
        public void setMessageReach(String messageReach) {
            this.messageReach = messageReach;
        }

        /**
         * check if line is started
         *
         * @return true if started
         */
        public boolean isStarted() {
            return started;
        }

        /**
         * start a line
         */
        public void setStarted() {
            started = true;
            startedLines.put(name, this);
            if (this.type.equalsIgnoreCase("finish")) {
                startedFinishLines.put(name, this);
            }
            if (startedFinishLines.size() == 1) {
                new LineListener(Lineation.getInstance());
            }
        }

        /**
         * stop a line
         */
        public void setStopped() {
            started = false;
            startedLines.remove(this.name);
            if (this.type.equalsIgnoreCase("finish")) {
                startedFinishLines.remove(this.name);
            }
            if (startedFinishLines.size() == 0) {
                LineListener.unregisterPluginEvents(Lineation.getInstance());
            }
        }

        /**
         * get linked line
         *
         * @return string linename
         */
        public String getLinkedLine() {
            return linkedLine;
        }

        /**
         * set linkedline
         *
         * @param string lineName
         */
        public void setLinkedLine(String lineName) {
            linkedLine = lineName;
        }

        /**
         * get lines world
         *
         * @return world the line is in
         */
        public World getWorld() {
            return world;
        }

        /**
         * get winners of finish line
         *
         * @return winners list
         */
        public ArrayList getWinners() {
            return winners;
        }

        /**
         * add winner to winners list
         *
         * @param Player player
         */
        public void addWinner(Player player) {
            winners.add(player.getName());
        }

        /**
         * sets winners list
         *
         * @param List strings
         */
        public void setWinners(ArrayList<String> winners) {
            this.winners = winners;
        }

        /**
         * clear winners list
         *
         */
        public void clearWinners() {
            winners.clear();
        }
        
        /**
         * check if player is winner
         *
         * @param player player
         */
        public boolean isWinner(Player player) {
            if (winners != null && !winners.isEmpty()) {
                if (winners.contains(player.getName())) {
                    return true;
                } else return false;
            } else return false;
        }

        /**
         * get maximum winners
         *
         * @return int
         */
        public int getMaxWinners() {
            return maxWinners;
        }

        /**
         * set maximum winners
         * 
         * @param int i
         */
        public void setMaxWinners(int i) {
            maxWinners = i;
        }

        /**
         * get allowed game modes
         * 
         * @return arraylist
         */
        public ArrayList<GameMode> getGameModes() {
            return allowedGameModes;
        }

        /**
         * add allowed game mode
         *
         * @param string gameModes
         */
        public void setGameModes(String gameModes) {
            String[] gameModesArray = gameModes.split(",");
            ArrayList<GameMode> allowedGameModes = new ArrayList<GameMode>();
            for (String g : gameModesArray) {
                allowedGameModes.add(GameMode.valueOf(g));
            }
            this.allowedGameModes = allowedGameModes;
        }

        /**
         * get game modes as string
         *
         * @return String
         */
        public String getGameModesString() {
            StringBuilder GameModes = new StringBuilder("");
            
            int i = 1;
            for (GameMode g : allowedGameModes) {
                if (i > 1) {
                    GameModes.append(",");
                }
                GameModes.append(g.toString());
                i++;
            }
            return GameModes.toString();
        }

        /**
         * get teleport location
         */
        public Location getTeleportLocation() {
            return teleportLocation;
        }

        /**
         * set teleport location
         */
        public void setTeleportLocation(Player player) {
            teleportLocation = player.getLocation();
        }

        /**
         * set teleport location
         */
        public void setTeleportLocation(Location loc) {
            teleportLocation = loc;
        }

        /**
         * get teleport enabled
         */
        public boolean isTeleportEnabled() {
            return teleportEnabled;
        }
        /**
         * set teleport enabled
         */
        public void setTeleportEnabled(boolean b) {
            teleportEnabled = b;
        }
        
        /**
         * set world for line
         *
         * @param world world
         */
        public void setWorld(World world) {
            this.world = world;
        }
        
        /**
         * get line area
         *
         * @return 2D array area
         */
        public double[][] getArea() {
            return area;
        }

        /**
         * use worldedit to set area of line
         *
         * @param player worldedit player
         */
        private void setArea(Player player, double[][] area) {
            WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

            com.sk89q.worldedit.regions.Region selection;
            try {
                selection = worldEdit.getSession(player).getSelection(worldEdit.getSession(player).getSelectionWorld());
            } catch (Exception e) {
                player.sendMessage(Message.ERROR_NULL_AREA);
                return;
            }

            if (selection != null) {
                area[0][0] = selection.getMinimumPoint().getX();
                area[0][1] = selection.getMinimumPoint().getY();
                area[0][2] = selection.getMinimumPoint().getZ();
                area[1][0] = selection.getMaximumPoint().getX();
                area[1][1] = selection.getMaximumPoint().getY();
                area[1][2] = selection.getMaximumPoint().getZ();
                world = BukkitAdapter.adapt(selection.getWorld());

                player.sendMessage(Message.SUCCESS_SET_AREA.replace("$LINE$", name));

            } else {
                player.sendMessage(Message.ERROR_NULL_AREA);
            }
        }

        /**
         * set line area
         *
         * @param area 2D array area
         */
        public void setArea(double[][] area) {
            this.area = area;
        }

        /**
         * set line area with worldedit
         *
         * @param player worldedit player
         */
        public void setArea(Player player) {
            setArea(player, area);
        }

        /**
         * use worldedit to add a border
         *
         * @param player worldedit player
         */
        private void addBorder(Player player, ArrayList<double[][]> borders) {
            WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

            com.sk89q.worldedit.regions.Region selection;
            try {
                selection = worldEdit.getSession(player).getSelection(worldEdit.getSession(player).getSelectionWorld());
            } catch (Exception e) {
                player.sendMessage(Message.ERROR_NULL_AREA);
                return;
            }
            double[][] border = new double[2][3];

            if (selection != null && world == BukkitAdapter.adapt(selection.getWorld())) {
                border[0][0] = selection.getMinimumPoint().getX();
                border[0][1] = selection.getMinimumPoint().getY();
                border[0][2] = selection.getMinimumPoint().getZ();
                border[1][0] = selection.getMaximumPoint().getX();
                border[1][1] = selection.getMaximumPoint().getY();
                border[1][2] = selection.getMaximumPoint().getZ();

                borders.add(border);
                player.sendMessage(Message.SUCCESS_SET_BORDER.replace("$LINE$", name));

            } else {
                player.sendMessage(Message.ERROR_NULL_AREA);
            }
        }

        /**
         * add line border
         * 
         * @param area 2D array area
         */
        public void addBorder(double[][] border) {
            borders.add(border);
        }

        /**
         * add line border
         *
         * @param player worldedit player
         */
        public void addBorder(Player player) {
            addBorder(player, borders);
        }
        
        /**
         * remove a border by number
         *
         * @param int i
         */
        public void removeBorder(int i) {
            borders.remove(i - 1);
        }

        /**
         * remove all borders
         */
        public void clearBorders() {
            borders.clear();
        }
            
        /**
         * get all borders
         */
        public ArrayList<double[][]> getBorders() {
            return borders;
        }

        /**
         * get blocksequence
         */
        public ArrayList<String> getBlockSequence() {
            return blockSequence;
        }

        /**
         * get blocksequence as string
         */
        public String getBlockSequenceString() {
            StringBuilder blockSequenceString = new StringBuilder("");

            int i = 1;
            for (String s : blockSequence) {
                if (i > 1) {
                    blockSequenceString.append(",");
                }
                blockSequenceString.append(s);
                i++;
            }
            return blockSequenceString.toString();
        }
        
        /**
         * set block sequence as string
         * @param string blocksequence
         */
        public void setBlockSequence(String blockSequence) {
            this.blockSequence = new ArrayList<String>(Arrays.asList(blockSequence.split("\\s*,\\s*")));
        }

        /**
         * check if location is in area
         *
         * @param l location to check
         * @return true if location is in area
         */
        public boolean contains(Location l) {
            try {
                double minx = area[0][0];
                double miny = area[0][1];
                double minz = area[0][2];
                double maxx = area[1][0];
                double maxy = area[1][1];
                double maxz = area[1][2];
                double tox = l.getBlock().getLocation().getX();
                double toy = l.getBlock().getLocation().getY();
                double toz = l.getBlock().getLocation().getZ();

                return (l.getWorld().equals(world) && (tox <= maxx) && (tox >= minx) && (toy <= maxy) &&
                        (toy >= miny) && (toz <= maxz) && (toz >= minz));
            } catch (Exception e) {
                return false;
            }
        }

        /**
         * get list of players in area
         *
         * @return list of players in area
         */
        public ArrayList<Player> getPlayers() {
            ArrayList<Player> players = new ArrayList<>();

            if (world != null)
                for (Player worldPlayer : world.getPlayers()) {
                    if (!worldPlayer.isOnline()) continue;
                    if(contains(worldPlayer.getLocation())) {
                        players.add(worldPlayer);
                    }
                }
            return players;
        }

        /**
         * check if player is in area
         * shouldnt be needed
         *
         * @param player player to see if area contains
         * @return true if player is in area
         */
        public boolean contains(Player player) {
            return getPlayers().contains(player);
        }
        
        /**
         * select area with worldedit
         *
         * @param player player to do the selecting
         */
        public void select(Player player) {
        
            try {
                BlockVector3 min = BlockVector3.at(area[0][0], area[0][1], area[0][2]);
                BlockVector3 max = BlockVector3.at(area[1][0], area[1][1], area[1][2]);

                com.sk89q.worldedit.entity.Player weplayer = BukkitAdapter.adapt(player);
                LocalSession ls = WorldEdit.getInstance().getSessionManager().get(weplayer);
                ls.getRegionSelector(BukkitAdapter.adapt(world)).selectPrimary(max, null);
                ls.getRegionSelector(BukkitAdapter.adapt(world)).selectSecondary(min,null);

                player.sendMessage(Message.SUCCESS_LINE_SELECTED.replace("$LINE$", name));
            } catch (Exception e) {
                player.sendMessage(Message.ERROR_UNKNOWN_LINE.replace("$LINE$", name));
            }
        }
        
        /**
         * get line the player is in
         *
         * @param player player to check for area
         * @return line player is on
         */
        public static Line get(Player player) {

            for (Line line : lines.values()) {
                    if (line.contains(player)) {
                        return line;
                    }
            }
            return null;
        }

        /**
         * get line from location
         *
         * @param l location to check
         * @return line location is in
         */
        public static Line get(Location l) {

            for (Line line : lines.values()) {
                if (line.contains(l)) {
                    return line;
                }
            }
            return null;
        }

}
