package com.diademiemi.lineation.line;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.diademiemi.lineation.Message;
import com.diademiemi.lineation.Lineation;

import java.util.ArrayList;
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
     * winners (only for finish type)
     */
    private ArrayList<Player> winners;

    /**
     * create line with defaults and provided name
     *
     * @param name line name
     */
    public Line(String name, String type) {
        this.name = name;
        this.type = type;
        started = false;
        world = Lineation.getInstance().getServer().getWorlds().get(0); 
        messageReach = "world";

        area = new double[2][3];
        area[0][0] = 0;
        area[0][1] = 0;
        area[0][2] = 0;
        area[1][0] = 0;
        area[1][1] = 0;
        area[1][2] = 0;

        if (type.equalsIgnoreCase("finish")) {
            winners = new ArrayList<Player>();
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
         * start or stop a line
         *
         * @param started true to start
         */
        public void setStarted(boolean started) {
            this.started = started;
            startedLines.put(name, this);
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
            winners.add(player);
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
                if (winners.contains(player)) {
                    return true;
                } else return false;
            } else return false;
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
