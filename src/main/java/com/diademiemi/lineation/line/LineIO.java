package com.diademiemi.lineation.line;

import com.diademiemi.lineation.Lineation;
import com.diademiemi.lineation.Config;

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
        Line line = new Line(name);
        if (lineConfig.getConfig().get(name + ".type" ) != null)
            line.setType(lineConfig.getConfig().getString(name + ".type"));
        if (lineConfig.getConfig().get(name + ".line") != null) {
            double[][] bounds = new double[2][3];
            bounds[0][0] = lineConfig.getConfig().getDouble(name + ".line.min.x");
            bounds[0][1] = lineConfig.getConfig().getDouble(name + ".line.min.y");
            bounds[0][2] = lineConfig.getConfig().getDouble(name + ".line.min.z");
            bounds[1][0] = lineConfig.getConfig().getDouble(name + ".line.max.x");
            bounds[1][1] = lineConfig.getConfig().getDouble(name + ".line.max.y");
            bounds[1][2] = lineConfig.getConfig().getDouble(name + ".line.max.z");
            String world = lineConfig.getConfig().getString(name + ".world");
            line.setBounds(bounds);
            line.setWorld(Lineation.getInstance().getServer().getWorld(world));
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
        if (line.getBounds() != null) {
            double[][] bounds = line.getBounds();
            lineConfig.getConfig().set(name + ".court.min.x", bounds[0][0]);
            lineConfig.getConfig().set(name + ".court.min.y", bounds[0][1]);
            lineConfig.getConfig().set(name + ".court.min.z", bounds[0][2]);
            lineConfig.getConfig().set(name + ".court.max.x", bounds[1][0]);
            lineConfig.getConfig().set(name + ".court.max.y", bounds[1][1]);
            lineConfig.getConfig().set(name + ".court.max.z", bounds[1][2]);
        }

        lineConfig.saveConfig();
    
    }

}
