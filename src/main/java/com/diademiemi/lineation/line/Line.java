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
     * HashMap of all lines
     */
    private static HashMap<String, Line> lines = new HashMap<>();

    /**
     * HashMap of all started lines, for easy access
     */
    private static HashMap<String, Line> startedLines = new HashMap<>();

    /**
     * HashMap of all started finish lines for easy access
     */
    private static HashMap<String, Line> startedFinishLines = new HashMap<>();

    /**
     * Name of the line
     */
    private String name;

    /**
     * Boolean to check if the line is started
     */
    private boolean started;

    /**
     * Area used for detection by the line
     */
    private double[][] area;

    /**
     * Borders for the line used to set blocks
     */
    private ArrayList<double[][]> borders;

    /**
     * List of block names used to open lines
     */
    private ArrayList<String> blockSequence;

    /**
     * World the line is in
     */
    private World world;

    /** 
     * Type of this line
     */
    private String type;

    /**
     * Reach of announcement messages by finish line
     */
    private String messageReach;

    /**
     * Name of linked line
     */
    private String linkedLine;

    /**
     * List of winner names
     */
    private ArrayList<String> winners;

    /**
     * Number of winners before theline closes
     */
    private Integer maxWinners;

    /**
     * List of game modes to check players in
     */
    private ArrayList<GameMode> allowedGameModes;

    /**
     * Boolean to check if teleportation is enabled
     */
    private boolean teleportEnabled;

    /**
     * Location to teleport players to
     */
    private Location teleportLocation;

    /**
     * create line with defaults and provided name
     *
     * @param name  The name of this line
     * @param type  What type this line is
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
         * Gets a HashMap of all loaded lines
         *
         * @return  HashMap of all lines
         */
        public static HashMap<String, Line> getLines() {
            return lines;
        }

        /**
         * Gets a HashMap of all start lines
         *
         * @return  HashMap of all start lines
         */
        public static HashMap<String, Line> getStartLines() {
            HashMap<String, Line> startLines = new HashMap<String, Line>();
            for(Map.Entry<String, Line> entry: lines.entrySet()) {
                if (entry.getValue().getType().equalsIgnoreCase("start")) {
                    startLines.put(entry.getKey(), entry.getValue());
                }
            }
            return startLines;
        }
        
        /**
         * Gets a HashMap of all finish lines
         *
         * @return  HashMap of all finish lines
         */
        public static HashMap<String, Line> getFinishLines() {
            HashMap<String, Line> finishLines = new HashMap<String, Line>();
            for(Map.Entry<String, Line> entry: lines.entrySet()) {
                if (entry.getValue().getType().equalsIgnoreCase("finish")) {
                    finishLines.put(entry.getKey(), entry.getValue());
                }
            }
            return finishLines;
        }

        /**
         * Gets HashMap of all started lines
         *
         * @return  HashMap of all started lines
         */
        public static HashMap<String, Line> getStartedLines() {
            return startedLines;
        }

        /**
         * Gets HashMap of all started finish lines
         *
         * @return  HashMap of all started finish lines
         */
        public static HashMap<String, Line> getStartedFinishLines() {
            return startedFinishLines;
        }


        /**
         * Get the name of this line
         *
         * @return  The name of this line
         */
        public String getName() {
            return name;
        }

        /**
         * Get the type of this line
         *
         * @return  The type of this line
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the type of this line
         *
         * @param   The type of this line
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * Gets the reach of messages of this line
         *
         * @return  Reach of messages for this line
         */
        public String getMessageReach() {
            return messageReach;
        }
        
        /**
         * Sets the reach of messages of this line
         *
         * @param   Reach of messages for this line
         */
        public void setMessageReach(String messageReach) {
            this.messageReach = messageReach;
        }

        /**
         * Check if this line is started
         *
         * @return  True if line is started
         */
        public boolean isStarted() {
            return started;
        }

        /**
         * Start a line, this adds the line to the startedLines HashMap and startedFinishLines if this line is a finish line.
         * If this is the only line started now, it also starts the listener for player movement.
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
         * Stops this line and removes it from startedLines HashMap and startedFinishLines HashMap.
         * If there are no started finish lines left, stop the listener for player movement.
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
         * Gets the name of the linked line
         *
         * @return  Name of the linked line
         */
        public String getLinkedLine() {
            return linkedLine;
        }

        /**
         * Sets the name of the linked line
         *
         * @param   Name of the linked line
         */
        public void setLinkedLine(String lineName) {
            linkedLine = lineName;
        }

        /**
         * Gets the world of this line
         * 
         * @return  World this line is in
         */
        public World getWorld() {
            return world;
        }

        /**
         * Sets the world of this line
         *
         * @param   World this line is in
         */
        public void setWorld(World world) {
            this.world = world;
        }
        
        /**
         * Gets the list of winner names
         *
         * @return  List of names of this lines last winners
         */
        public ArrayList getWinners() {
            return winners;
        }

        /**
         * Add name of player to the winners list
         *
         * @param   Player to add to the list
         */
        public void addWinner(Player player) {
            winners.add(player.getName());
        }

        /**
         * Sets the list of winners of this line
         *
         * @param   List of winner names
         */
        public void setWinners(ArrayList<String> winners) {
            this.winners = winners;
        }

        /**
         * Clear the list of winners for this line
         */
        public void clearWinners() {
            winners.clear();
        }
        
        /**
         * Check if this player is a winner in this line already
         *
         * @param   Player to check
         */
        public boolean isWinner(Player player) {
            if (winners != null && !winners.isEmpty()) {
                if (winners.contains(player.getName())) {
                    return true;
                } else return false;
            } else return false;
        }

        /**
         * Gets the maximum amount of winners of this line
         *
         * @return  Number of winners before the line closes
         */
        public int getMaxWinners() {
            return maxWinners;
        }

        /**
         * Sets the maximum amount of winners of this line
         * 
         * @param   Number of winners before the line closes
         */
        public void setMaxWinners(int i) {
            maxWinners = i;
        }

        /**
         * Gets list of allowed game modes
         * 
         * @return  ArrayList of allowed gamemodes of this line
         */
        public ArrayList<GameMode> getGameModes() {
            return allowedGameModes;
        }

        /**
         * Sets the list of allowed game modes
         *
         * @param   Comma seperated string of gamemode names
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
         * Gets the allowed game modes as string
         *
         * @return  Comma seperated string of gamemode names
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
         * Gets the teleport location
         *
         * @return  Location to teleport players to on finish
         */
        public Location getTeleportLocation() {
            return teleportLocation;
        }

        /**
         * Sets the teleport location to players current location
         *
         * @param   Player to get the location from
         */
        public void setTeleportLocation(Player player) {
            teleportLocation = player.getLocation();
        }

        /**
         * Sets the teleport location to this location
         *
         * @param   Location to set the teleport location to
         */
        public void setTeleportLocation(Location loc) {
            teleportLocation = loc;
        }

        /**
         * Gets boolean of if teleport is enabled
         *
         * @return  True if teleport is enabled
         */
        public boolean isTeleportEnabled() {
            return teleportEnabled;
        }
        /**
         * Sets whether teleport is enabled
         *
         * @param   Boolean of if teleport is enabled
         */
        public void setTeleportEnabled(boolean b) {
            teleportEnabled = b;
        }
        
        /**
         * Gets the area of this line
         *
         * @return Double of the area of this line
         */
        public double[][] getArea() {
            return area;
        }

        /**
         * Use WorldEdit to set the area of this line
         *
         * @param   Player to get WorldEdit selection from
         * @param   Area to set 
         */
        public void setArea(Player player) {
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
         * Sets the area of this line
         *
         * @param   Double of the area of this line
         */
        public void setArea(double[][] area) {
            this.area = area;
        }
            
        /**
         * Get list of all borders of this line
         *
         * @return  ArrayList of all borders of this line
         */
        public ArrayList<double[][]> getBorders() {
            return borders;
        }

        /**
         * Use WorldEdit to add a border to this line
         *
         * @param   Player to get WorldEdit selection from
         */
        public void addBorder(Player player) {
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
         * Add a border to this line
         * 
         * @param   Double of the border to add to this line
         */
        public void addBorder(double[][] border) {
            borders.add(border);
        }
        
        /**
         * Remove a border by number
         *
         * @param   Number of the border to remove
         */
        public void removeBorder(int i) {
            borders.remove(i - 1);
        }

        /**
         * Remove all borders of this line
         */
        public void clearBorders() {
            borders.clear();
        }

        /**
         * Get this lines block sequence
         *
         * @return  ArrayList of block names
         */
        public ArrayList<String> getBlockSequence() {
            return blockSequence;
        }

        /**
         * Get this lines block sequence as a string
         *
         * @return  Comma seperated list of block names
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
         * Sets this lines block sequence
         *
         * @param   Comma seperated list of block names
         */
        public void setBlockSequence(String blockSequence) {
            this.blockSequence = new ArrayList<String>(Arrays.asList(blockSequence.split("\\s*,\\s*")));
        }

         /**
         * Check if this player is in this area
         *
         * @param   Player to check location of
         * @return  Boolean of if this player is in the area
         */
        public boolean contains(Player player) {
            return getPlayers().contains(player);
        }

        /**
         * Check if this location is in this area
         *
         * @param   Location to check
         * @return  Boolean of if this location is in the area
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
         * Get a list of players in this area
         *
         * @return  List of players in this area
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
         * Gets the line this player is in
         *
         * @param   Player to check location of
         * @return  Line this player is in
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
         * Gets the line this location is in
         *
         * @param   Location to check 
         * @return  Line this location is in
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
