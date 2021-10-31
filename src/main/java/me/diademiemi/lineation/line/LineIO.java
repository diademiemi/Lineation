package me.diademiemi.lineation.line;

import me.diademiemi.lineation.Lineation;
import me.diademiemi.lineation.Config;

import java.util.ArrayList;
/**
 * Class to load and save lines to file
 *
 * @author diademiemi
 */
public class LineIO {

    /**
     * Line config instance
     */
    private static final Config lineConfig = Config.getLineConfig();

    /**
     * Load all lines in the config file
     */
    public static void loadAll() {
        for (String name : lineConfig.getConfig().getKeys(false)) {
            loadLine(name);
        }
    }

    /**
     * Load a line by name
     *
     * @param name  Name of the line to load
     */
    private static void loadLine(String name) {
        String type = lineConfig.getConfig().getString(name + ".type");
        Line line = new Line(name, type);
        if (lineConfig.getConfig().get(name + ".type" ) != null)
            line.setType(lineConfig.getConfig().getString(name + ".type"));
        if (lineConfig.getConfig().get(name + ".option.messagereach" ) != null)
            line.setMessageReach(lineConfig.getConfig().getString(name + ".option.messagereach"));
        if (lineConfig.getConfig().get(name + ".option.blocksequence" ) != null)
            line.setBlockSequence(lineConfig.getConfig().getString(name + ".option.blocksequence"));
        if (lineConfig.getConfig().get(name + ".option.linked" ) != null)
            line.setLinkedLine(lineConfig.getConfig().getString(name + ".option.linked"));

        if (lineConfig.getConfig().get(name + ".option.teleport.enabled") != null) {
            line.setTeleportEnabled(lineConfig.getConfig().getBoolean(name + ".option.teleport.enabled"));
            if (lineConfig.getConfig().get(name + ".option.teleport.location") != null) {
				line.setTeleportLocation(lineConfig.getConfig().getVector(name + ".option.teleport.location")
                    .toLocation(Lineation.getInstance().getServer()
                    .getWorld(lineConfig.getConfig().getString(name + ".world")),
                    (float) lineConfig.getConfig().getDouble(name + ".option.teleport.yaw"),
                    (float) lineConfig.getConfig().getDouble(name + ".option.teleport.pitch")));
			}

        }

        if (lineConfig.getConfig().get(name + ".area") != null) {
            double[][] area = new double[2][3];
            area[0][0] = lineConfig.getConfig().getDouble(name + ".area.min.x");
            area[0][1] = lineConfig.getConfig().getDouble(name + ".area.min.y");
            area[0][2] = lineConfig.getConfig().getDouble(name + ".area.min.z");
            area[1][0] = lineConfig.getConfig().getDouble(name + ".area.max.x");
            area[1][1] = lineConfig.getConfig().getDouble(name + ".area.max.y");
            area[1][2] = lineConfig.getConfig().getDouble(name + ".area.max.z");
            String world = lineConfig.getConfig().getString(name + ".world");
            line.setArea(area);
            line.setWorld(Lineation.getInstance().getServer().getWorld(world));
        }
        
        int i = 1;
        while (lineConfig.getConfig().get(name + ".border." + i + ".min.x" ) != null) {
            double[][] b = new double[2][3];
            b[0][0] = lineConfig.getConfig().getDouble(name +  ".border." + i + ".min.x");
            b[0][1] = lineConfig.getConfig().getDouble(name +  ".border." + i + ".min.y");
            b[0][2] = lineConfig.getConfig().getDouble(name +  ".border." + i + ".min.z");
            b[1][0] = lineConfig.getConfig().getDouble(name +  ".border." + i + ".max.x");
            b[1][1] = lineConfig.getConfig().getDouble(name +  ".border." + i + ".max.y");
            b[1][2] = lineConfig.getConfig().getDouble(name +  ".border." + i + ".max.z");
            line.addBorder(b);
            i++;
        }

        if (type.equalsIgnoreCase("finish")) {
            if (lineConfig.getConfig().get(name + ".lastwinners") != null) {
                line.setWinners((ArrayList<String>)lineConfig.getConfig().getStringList(name + ".lastwinners"));
            }
            if (lineConfig.getConfig().get(name + ".option.maxwinners") != null) {
                line.setMaxWinners(lineConfig.getConfig().getInt(name + ".option.maxwinners"));
            }
            if (lineConfig.getConfig().get(name + ".option.commands") != null) {
                line.setCommands(lineConfig.getConfig().getStringList(name + ".option.commands"));
            }
            if (lineConfig.getConfig().get(name + ".option.gamemodes") != null) {
                line.setGameModes(lineConfig.getConfig().getString(name + ".option.gamemodes"));
            }
            if (lineConfig.getConfig().get(name + ".option.laps") != null) {
                line.setLaps(lineConfig.getConfig().getInt(name + ".option.laps"));
            }

            i = 1;
            while (lineConfig.getConfig().get(name + ".checkpoint." + i + ".min.x" ) != null) {
                double[][] c = new double[2][3];
                c[0][0] = lineConfig.getConfig().getDouble(name +  ".checkpoint." + i + ".min.x");
                c[0][1] = lineConfig.getConfig().getDouble(name +  ".checkpoint." + i + ".min.y");
                c[0][2] = lineConfig.getConfig().getDouble(name +  ".checkpoint." + i + ".min.z");
                c[1][0] = lineConfig.getConfig().getDouble(name +  ".checkpoint." + i + ".max.x");
                c[1][1] = lineConfig.getConfig().getDouble(name +  ".checkpoint." + i + ".max.y");
                c[1][2] = lineConfig.getConfig().getDouble(name +  ".checkpoint." + i + ".max.z");
                line.addCheckpoint(c);
                i++;
            }


        }
    }
    
    /** 
     * Save all lines to config file
     */
    public static void saveAll() {
        for (String key : lineConfig.getConfig().getKeys(false)) {
            lineConfig.getConfig().set(key, null);
        }

        for (Line line : Line.getLines().values()) {
            saveLine(line);
        }

        lineConfig.saveConfig();

    }

    /**
     * Save a line to config file by name
     *
     * @param line  Line to save
     */
    private static void saveLine(Line line) {
        String name = line.getName();

        lineConfig.getConfig().set(name + ".type", line.getType());
        lineConfig.getConfig().set(name + ".option.messagereach", line.getMessageReach());
        lineConfig.getConfig().set(name + ".option.blocksequence", line.getBlockSequenceString());
        lineConfig.getConfig().set(name + ".option.linked", line.getLinkedLine());
        lineConfig.getConfig().set(name + ".option.teleport.enabled", line.isTeleportEnabled());
        if (line.getTeleportLocation() != null) lineConfig.getConfig().set(name + ".option.teleport.location", line.getTeleportLocation().toVector());
        if (line.getTeleportLocation() != null) lineConfig.getConfig().set(name + ".option.teleport.yaw", (double) line.getTeleportLocation().getYaw());
        if (line.getTeleportLocation() != null) lineConfig.getConfig().set(name + ".option.teleport.pitch", (double) line.getTeleportLocation().getPitch());

        if (line.getArea() != null) {
            double[][] area = line.getArea();
            lineConfig.getConfig().set(name + ".area.min.x", area[0][0]);
            lineConfig.getConfig().set(name + ".area.min.y", area[0][1]);
            lineConfig.getConfig().set(name + ".area.min.z", area[0][2]);
            lineConfig.getConfig().set(name + ".area.max.x", area[1][0]);
            lineConfig.getConfig().set(name + ".area.max.y", area[1][1]);
            lineConfig.getConfig().set(name + ".area.max.z", area[1][2]);
            lineConfig.getConfig().set(name + ".world", line.getWorld().getName());
        }
        if (line.getBorders() != null) {
            int i = 1;
            for (double[][] b : line.getBorders()) {
                lineConfig.getConfig().set(name + ".border." + i + ".min.x", b[0][0]);
                lineConfig.getConfig().set(name + ".border." + i + ".min.y", b[0][1]);
                lineConfig.getConfig().set(name + ".border." + i + ".min.z", b[0][2]);
                lineConfig.getConfig().set(name + ".border." + i + ".max.x", b[1][0]);
                lineConfig.getConfig().set(name + ".border." + i + ".max.y", b[1][1]);
                lineConfig.getConfig().set(name + ".border." + i + ".max.z", b[1][2]);
                i++;
            }
        }

        if (line.getCheckpoints() != null) {
            int i = 1;
            for (double[][] c : line.getCheckpoints()) {
                lineConfig.getConfig().set(name + ".checkpoint." + i + ".min.x", c[0][0]);
                lineConfig.getConfig().set(name + ".checkpoint." + i + ".min.y", c[0][1]);
                lineConfig.getConfig().set(name + ".checkpoint." + i + ".min.z", c[0][2]);
                lineConfig.getConfig().set(name + ".checkpoint." + i + ".max.x", c[1][0]);
                lineConfig.getConfig().set(name + ".checkpoint." + i + ".max.y", c[1][1]);
                lineConfig.getConfig().set(name + ".checkpoint." + i + ".max.z", c[1][2]);
                i++;
            }
        }

        if (line.getType().equalsIgnoreCase("finish")) {

            lineConfig.getConfig().set(name + ".lastwinners", line.getWinners());
            lineConfig.getConfig().set(name + ".option.maxwinners", line.getMaxWinners());
            lineConfig.getConfig().set(name + ".option.commands", line.getCommands());
            lineConfig.getConfig().set(name + ".option.gamemodes", line.getGameModesString());
            lineConfig.getConfig().set(name + ".option.laps", line.getLaps());
        }

    }

}
