package me.diademiemi.lineation.line;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import me.diademiemi.lineation.Message;
import me.diademiemi.lineation.config.Config;
import me.diademiemi.lineation.listeners.MoveEvents;
import me.diademiemi.lineation.listeners.Scheduled;
import me.diademiemi.lineation.Lineation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

    /**
     * Lineation line handler
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
	 * HashMap of all started start lines with illegal areas
	 */
	private static HashMap<String, Line> startedStartLinesIA = new HashMap<>();

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
     * Borders for the invisible checkpoints
     */
    private ArrayList<double[][]> checkpoints;

	/**
	 * Illegal areas for start lines
	 */
	private ArrayList<double[][]> illegalAreas;

    /**
     * HashMap of player and checkpoint counter
     */
    private HashMap<Player,Integer> checkpointCount;

    /**
     * Integer of amount of laps
     */
    private Integer laps;

    /**
     * HashMap of player and laps counter
     */
    private HashMap<Player,Integer> lapCount;

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
     * Boolean to check if teleportation is enabled for illegal areas
     */
    private boolean teleportEnabledIllegalArea;

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
        this.setGameModes(Config.getPluginConfig().getConfig().getString("linedefaults.option.gamemodes"));
        this.teleportLocation = new Location(world, 0, 0, 0, 0, 0);

        if (type.equalsIgnoreCase("finish")) {
            winners = new ArrayList<String>();
            maxWinners = Config.getPluginConfig().getConfig().getInt("linedefaults.option.maxwinners");
            laps = 1;
            checkpoints = new ArrayList<double[][]>();
            checkpointCount = new HashMap<Player, Integer>();
            lapCount = new HashMap<Player, Integer>();
        }
		if (type.equalsIgnoreCase("start")) {
			illegalAreas = new ArrayList<double[][]>();
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
		 * Gets HashMap of all started start lines with illegal areas
		 *
		 * @return	HashMap of all started start lines with illegal areas
		 */
		public static HashMap<String, Line> getStartedStartLinesIA() {
			return startedStartLinesIA;
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
         * @param type   The type of this line
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
         * @param messageReach   Reach of messages for this line
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
			if (this.type.equalsIgnoreCase("start")) {
				if (teleportEnabledIllegalArea) {
					if (this.getIllegalAreas().size() != 0) {
						startedStartLinesIA.put(name, this);
					}
				}
			}
            if (startedFinishLines.size() == 1 || startedStartLinesIA.size() == 1) {
                if (Config.getPluginConfig().getConfig().getBoolean("aggressive-listener")) {
                    Scheduled.checkMovements();
                } else {
                    new MoveEvents(Lineation.getInstance());
                }
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

            if (startedFinishLines.size() == 0 && startedStartLinesIA.size() == 0) {
                if (Config.getPluginConfig().getConfig().getBoolean("aggressive-listener")) {
                    Scheduled.cancelTasks(Lineation.getInstance());
                } else {
                    MoveEvents.unregisterPluginEvents(Lineation.getInstance());
                }
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
         * @param lineName  Name of the linked line
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
         * @param world   World this line is in
         */
        public void setWorld(World world) {
            this.world = world;
        }
        
        /**
         * Gets the list of winner names
         *
         * @return  List of names of this lines last winners
         */
        public ArrayList<String> getWinners() {
            return winners;
        }

        /**
         * Add name of player to the winners list
         *
         * @param player   Player to add to the list
         */
        public void addWinner(Player player) {
            winners.add(player.getName());
        }

        /**
         * Sets the list of winners of this line
         *
         * @param winners   List of winner names
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
         * @param player   Player to check
         * @return  Boolean of if winner
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
         * @param i   Number of winners before the line closes
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
         * @param gameModes   Comma seperated string of gamemode names
         */
        public boolean setGameModes(String gameModes) {

			if (LineTools.validateGamemodes(gameModes)) {

				String[] gameModesArray = gameModes.split(",");
				ArrayList<GameMode> allowedGameModes = new ArrayList<GameMode>();

				for (String g : gameModesArray) {
					allowedGameModes.add(GameMode.valueOf(g));
				}

				this.allowedGameModes = allowedGameModes;

			} else {
				return false;
			}

			return true;

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
         * @param player   Player to get the location from
         */
        public void setTeleportLocation(Player player) {
            teleportLocation = player.getLocation();
        }

        /**
         * Sets the teleport location to this location
         *
         * @param loc   Location to set the teleport location to
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
         * @param b   Boolean of if teleport is enabled
         */
        public void setTeleportEnabled(boolean b) {
            teleportEnabled = b;
        }

        /**
         * Gets boolean of if teleport is enabled on illegal areas
         *
         * @return  True if teleport is enabled for illegal areas
         */
        public boolean isTeleportEnabledIllegalArea() {
            return teleportEnabledIllegalArea;
        }
        /**
         * Sets whether teleport is enabled
         *
         * @param b   Boolean of if teleport is enabled
         */
        public void setTeleportEnabledIllegalArea(boolean b) {
            teleportEnabledIllegalArea = b;
			if (b) {
				if (this.isStarted()) {
					startedStartLinesIA.put(name, this);
				}
			} else {
				startedStartLinesIA.remove(name);
			}
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
         * @param player   Player to get WorldEdit selection from
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
         * @param area   Double of the area of this line
         */
        public void setArea(double[][] area) {
            this.area = area;
        }

		/**
		 * Set the area of this line to the players location.
		 * This is just a temporary area until it's defined manually, but this removes some possible error.
		 *
		 * @param loc	Location to set the area to
		 */
		public void setArea(Location loc) {
			area[0][0] = loc.getBlockX();
			area[0][1] = loc.getBlockY();
			area[0][2] = loc.getBlockZ();
			area[1][0] = loc.getBlockX();
			area[1][1] = loc.getBlockY();
			area[1][2] = loc.getBlockZ();
			world = loc.getWorld();
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
         * @param player   Player to get WorldEdit selection from
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
                LineTools.stopLine(this);
                player.sendMessage(Message.SUCCESS_SET_BORDER.replace("$LINE$", name));

            } else {
                player.sendMessage(Message.ERROR_NULL_AREA);
            }
        }

        /**
         * Add a border to this line
         * 
         * @param border   Double of the border to add to this line
         */
        public void addBorder(double[][] border) {
            borders.add(border);
        }
        
        /**
         * Remove a border by number
         *
         * @param i   Number of the border to remove
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
         * Get list of all checkpoints of this line
         *
         * @return  ArrayList of all checkpoints of this line
         */
        public ArrayList<double[][]> getCheckpoints() {
            return checkpoints;
        }

        /**
         * Use WorldEdit to add a checkpoint to this line
         *
         * @param player   Player to get WorldEdit selection from
         */
        public void addCheckpoint(Player player) {
            WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

            com.sk89q.worldedit.regions.Region selection;
            try {
                selection = worldEdit.getSession(player).getSelection(worldEdit.getSession(player).getSelectionWorld());
            } catch (Exception e) {
                player.sendMessage(Message.ERROR_NULL_AREA);
                return;
            }
            double[][] checkpoint = new double[2][3];

            if (selection != null && world == BukkitAdapter.adapt(selection.getWorld())) {
                checkpoint[0][0] = selection.getMinimumPoint().getX();
                checkpoint[0][1] = selection.getMinimumPoint().getY();
                checkpoint[0][2] = selection.getMinimumPoint().getZ();
                checkpoint[1][0] = selection.getMaximumPoint().getX();
                checkpoint[1][1] = selection.getMaximumPoint().getY();
                checkpoint[1][2] = selection.getMaximumPoint().getZ();

                checkpoints.add(checkpoint);
                player.sendMessage(Message.SUCCESS_SET_CHECKPOINT.replace("$LINE$", name));

            } else {
                player.sendMessage(Message.ERROR_NULL_AREA);
            }
        }

        /**
         * Add a checkpoint to this line
         * 
         * @param checkpoint   Double of the checkpoint to add to this line
         */
        public void addCheckpoint(double[][] checkpoint) {
            checkpoints.add(checkpoint);
        }
        
        /**
         * Remove a checkpoint by number
         *
         * @param i   Number of the checkpoint to remove
         */
        public void removeCheckpoint(int i) {
            checkpoints.remove(i - 1);
        }

        /**
         * Remove all checkpoints of this line
         */
        public void clearCheckpoints() {
            checkpoints.clear();
        }

        /**
         * Get list of all illegalAreas of this line
         *
         * @return  ArrayList of all illegal areas of this line
         */
        public ArrayList<double[][]> getIllegalAreas() {
            return illegalAreas;
        }

        /**
         * Use WorldEdit to add an illegal area to this line
         *
         * @param player   Player to get WorldEdit selection from
         */
        public void addIllegalArea(Player player) {
            WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

            com.sk89q.worldedit.regions.Region selection;
            try {
                selection = worldEdit.getSession(player).getSelection(worldEdit.getSession(player).getSelectionWorld());
            } catch (Exception e) {
                player.sendMessage(Message.ERROR_NULL_AREA);
                return;
            }
            double[][] ia = new double[2][3];

            if (selection != null && world == BukkitAdapter.adapt(selection.getWorld())) {
                ia[0][0] = selection.getMinimumPoint().getX();
                ia[0][1] = selection.getMinimumPoint().getY();
				ia[0][2] = selection.getMinimumPoint().getZ();
                ia[1][0] = selection.getMaximumPoint().getX();
                ia[1][1] = selection.getMaximumPoint().getY();
                ia[1][2] = selection.getMaximumPoint().getZ();

                illegalAreas.add(ia);
                player.sendMessage(Message.SUCCESS_SET_ILLEGAL_AREA.replace("$LINE$", name));

            } else {
                player.sendMessage(Message.ERROR_NULL_AREA);
            }
        }

        /**
         * Add an illegal area to this line
         * 
         * @param ia	Double of the illegal area to add to this line
         */
        public void addIllegalArea(double[][] ia) {
			illegalAreas.add(ia);
        }
        
        /**
         * Remove an illegal area by number
         *
         * @param i   Number of the illegal area to remove
         */
        public void removeIllegalArea(int i) {
			illegalAreas.remove(i - 1);
        }

        /**
         * Remove all illegal areas of this line
         */
        public void clearIllegalAreas() {
            illegalAreas.clear();
        }

        /**
         * Gets map of player checkpoints
         *
         * @param player    Player to get count of
         * @return  Map of player and checkpoins reached
         */
        public int getPlayerCheckpoint(Player player) {
            if (!checkpointCount.keySet().contains(player)) return 0;
            return checkpointCount.get(player);
        }

        /**
         * Adds to players checkpoint count
         *
         * @param player    Player to add count of
         * @param i Integer to set count to
         */
        public void addPlayerCheckpoint(Player player, int i) {
            checkpointCount.put(player, i);
        }

        /**
         * Clears the player checkpoint counter
         */
        public void clearPlayerCheckpoints() {
            checkpointCount.clear();
        }

        /**
         * Gets amount of laps for this line
         *
         * @return  Integer of amount of laps
         */
        public int getLaps() {
            return laps;
        }

        /**
         * Sets amount of laps for this line
         *
         * @param i Integer to set laps to
         */
        public void setLaps(int i) {
            laps = i;
        }

        /**
         * Gets map of player laps
         *
         * @param player    Player to get count of
         * @return  Map of player and laps done
         */
        public int getPlayerLaps(Player player) {
            if (!lapCount.keySet().contains(player)) return 0;
            return lapCount.get(player);
        }

        /**
         * Adds to players laps count
         *
         * @param player    Player to add count of
         * @param i Integer to set count to
         */
        public void addPlayerLap(Player player, int i) {
            lapCount.put(player, i);
        }

        /**
         * Clears the player laps counter
         */
        public void clearPlayerLaps() {
            lapCount.clear();
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
                    blockSequenceString.append(", ");
                }
                blockSequenceString.append(s);
                i++;
            }
            return blockSequenceString.toString();
        }
        
        /**
         * Sets this lines block sequence
         *
         * @param blockSequence   Comma seperated list of block names
		 *
		 * @return	If action was successful
         */
        public boolean setBlockSequence(String blockSequence) {

			if (LineTools.validateBlocks(blockSequence)){

				if (this.getType().equalsIgnoreCase("finish")) {
					this.blockSequence = new ArrayList<String>(Arrays.asList(blockSequence.split("\\s*,\\s*")[0]));
				} else {
					this.blockSequence = new ArrayList<String>(Arrays.asList(blockSequence.split("\\s*,\\s*")));
				}

			} else {
				return false;
			}

			return true;

		}

         /**
         * Check if this player is in this area
         *
         * @param player   Player to check location of
         * @return  Boolean of if this player is in the area
         */
        public boolean contains(Player player) {
            return contains(player.getLocation());
        }

        /**
         * Check if this location is in this area
         *
         * @param l   Location to check
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
         * @param player   Player to check location of
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
         * @param l   Location to check 
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

        /**
         * Get the checkpoint this player is in
         *
         * @param player    Player to check the location of
         * @return  Integer of the checkpoint the player is in
         */
        public int checkpointsContain(Player player) {
            return checkpointsContain(player.getLocation());
        }

        /**
         * Get the checkpoint this location is in
         *
         * @param l Location to check
         * @return  Integer of the checkpoint this location is in
         */
        public int checkpointsContain(Location l) {
            int i = 1;
            for (double[][] c : checkpoints) {
                double minx = c[0][0];
                double miny = c[0][1];
                double minz = c[0][2];
                double maxx = c[1][0];
                double maxy = c[1][1];
                double maxz = c[1][2];
                double tox = l.getBlock().getLocation().getX();
                double toy = l.getBlock().getLocation().getY();
                double toz = l.getBlock().getLocation().getZ();

                if (l.getWorld().equals(world) && (tox <= maxx) && (tox >= minx) && (toy <= maxy) &&
                        (toy >= miny) && (toz <= maxz) && (toz >= minz)) return i;
                i++;
            } 
            return 0;
        }

        /**
         * Get if this player is in an illegal area
         *
         * @param player    Player to check the location of
         * @return	Boolean of whether player is in an illegal area
         */
        public boolean illegalAreaContains(Player player) {
            return illegalAreaContains(player.getLocation());
        }

        /**
         * Get if this location is in an illegal area
         *
         * @param l Location to check
         * @return  Boolean of whether this location is in an illegal area
         */
        public boolean illegalAreaContains(Location l) {
            for (double[][] c : illegalAreas) {
                double minx = c[0][0];
                double miny = c[0][1];
                double minz = c[0][2];
                double maxx = c[1][0];
                double maxy = c[1][1];
                double maxz = c[1][2];
                double tox = l.getBlock().getLocation().getX();
                double toy = l.getBlock().getLocation().getY();
                double toz = l.getBlock().getLocation().getZ();

                if (l.getWorld().equals(world) && (tox <= maxx) && (tox >= minx) && (toy <= maxy) &&
                        (toy >= miny) && (toz <= maxz) && (toz >= minz)) return true;
                }

            return false;
        }

}
