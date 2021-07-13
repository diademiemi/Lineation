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
     * line name
     */
    private String name;

    /**
     * started yes or no
     */
    private boolean started;

    /**
     * line bounds
     */
    private double[][] bounds;

    /**
     * world
     */
    private World world;

    /** 
     * line type (start/finish)
     */
    private String type;

    /**
     * create line with defaults and provided name
     *
     * @param name line name
     */
    public Line(String type, String name) {
        this.type = type;
        this.name = name;
        started = false;
        world = Lineation.getInstance().getServer().getWorlds().get(0);

        bounds = new double[2][3];
        bounds[0][0] = 0;
        bounds[0][1] = 0;
        bounds[0][2] = 0;
        bounds[1][0] = 0;
        bounds[1][1] = 0;
        bounds[1][2] = 0;

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
            return lines;
        }

        public static HashMap<String, Line> getFinishLines() {
            return lines;
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
         * set world for line
         *
         * @param world world
         */
        public void setWorld(World world) {
            this.world = world;
        }
        
        /**
         * get line bounds
         *
         * @return 2D array bounds
         */
        public double[][] getBounds() {
            return bounds;
        }

        /**
         * use worldedit to set bounds of line
         *
         * @param player worldedit player
         */
        private void setBounds(Player player, double[][] bounds) {
            WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

            com.sk89q.worldedit.regions.Region selection;
            try {
                selection = worldEdit.getSession(player).getSelection(worldEdit.getSession(player).getSelectionWorld());
            } catch (Exception e) {
                player.sendMessage(Message.ERROR_NULL_BOUNDS);
                return;
            }

            if (selection != null) {
                bounds[0][0] = selection.getMinimumPoint().getX();
                bounds[0][1] = selection.getMinimumPoint().getY();
                bounds[0][2] = selection.getMinimumPoint().getZ();
                bounds[1][0] = selection.getMaximumPoint().getX();
                bounds[1][1] = selection.getMaximumPoint().getY();
                bounds[1][2] = selection.getMaximumPoint().getZ();
                world = BukkitAdapter.adapt(selection.getWorld());

                player.sendMessage(Message.SUCCESS_SET_BOUNDS.replace("$LINE$", name));

            } else {
                player.sendMessage(Message.ERROR_NULL_BOUNDS);
            }
        }

        /**
         * set line bounds
         *
         * @param bounds 2D array bounds
         */
        public void setBounds(double[][] bounds) {
            this.bounds = bounds;
        }

        /**
         * set line bounds with worldedit
         *
         * @param player worldedit player
         */
        public void setBounds(Player player) {
            setBounds(player, bounds);
        }

        /**
         * check if location is in bounds
         *
         * @param l location to check
         * @return true if location is in bounds
         */
        public boolean contains(Location l) {
            try {
                double minx = bounds[0][0];
                double miny = bounds[0][1];
                double minz = bounds[0][2];
                double maxx = bounds[1][0];
                double maxy = bounds[1][1];
                double maxz = bounds[1][2];
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
         * get list of players in bounds
         *
         * @return list of players in bounds
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
         * check if player is in bounds
         * shouldnt be needed
         *
         * @param player player to see if bounds contains
         * @return true if player is in bounds
         */
        public boolean contains(Player player) {
            return getPlayers().contains(player);
        }
        
        /**
         * select bounds with worldedit
         *
         * @param player player to do the selecting
         */
        public void select(Player player) {
        
            try {
                BlockVector3 min = BlockVector3.at(bounds[0][0], bounds[0][1], bounds[0][2]);
                BlockVector3 max = BlockVector3.at(bounds[1][0], bounds[1][1], bounds[1][2]);

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
         * message the line using formatted LINE_INFO message
         * 
         * @param player player user
         * @param string line name
         */
        public void getLineInfo(Player player, String name) {
            
            try {
                player.sendMessage(Message.LINE_INFO
                        .replace("$NAME$", name)
                        .replace("$STARTED$", Boolean.toString(isStarted()))
                        .replace("$WORLD$", world.getName())
                        .replace("$BOUNDS$",
                            "(" + bounds[0][0] + "," + bounds[0][1] + "," + bounds[0][2] + "} -> )" +
                            bounds[1][0] + "," + bounds[1][1] + "," + bounds[1][2] + ")"));
            } catch (Exception e) {
                player.sendMessage(Message.ERROR_UNKNOWN_LINE.replace("$LINE$", name));
            }
        }

        /**
         * get line the player is in
         *
         * @param player player to check for bounds
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
