package com.diademiemi.lineation.line;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.diademiemi.lineation.Lineation;
import com.diademiemi.lineation.Config;

import java.util.ArrayList;
import java.util.List;
/**
 * Class to load and save lines to file
 *
 * @author diademiemi
 */
public class LineIO {

    /**
     * line config instance
     */
    private static final Config lineConfig = Config.getLineConfig();

    /**
     * load lines in file
     */
    public static void loadAll() {
        for (String name : lineConfig.getConfig().getKeys(false)) {
            loadLine(name);
        }
    }

    /**
     * load a line by name
     *
     * @param name name of line to load
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
        double[][] b = new double[2][3];
        while (lineConfig.getConfig().get(name + ".border." + i + ".min.x" ) != null) {
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
            if (lineConfig.getConfig().get(name + ".option.gamemodes") != null) {
                line.setGameModes(lineConfig.getConfig().getString(name + ".option.gamemodes"));
            }
        }
    }
    
    /** 
     * save all lines to file
     */
    public static void saveAll() {
        for (String key : lineConfig.getConfig().getKeys(false)) {
            lineConfig.getConfig().set(key, null);
        }

        for (Line line : Line.getLines().values()) {
            saveLine(line);
        }
    }

    /**
     * save line to file
     *
     * @param line line to save
     */
    private static void saveLine(Line line) {
        String name = line.getName();

        lineConfig.getConfig().set(name + ".type", line.getType());
        lineConfig.getConfig().set(name + ".option.messagereach", line.getMessageReach());
        lineConfig.getConfig().set(name + ".option.blocksequence", line.getBlockSequenceString());

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

        if (line.getType().equalsIgnoreCase("finish")) {

            lineConfig.getConfig().set(name + ".lastwinners", line.getWinners());
            lineConfig.getConfig().set(name + ".option.maxwinners", line.getMaxWinners());
            lineConfig.getConfig().set(name + ".option.gamemodes", line.getGameModesString());
        }

        lineConfig.saveConfig();
    
    }

}
