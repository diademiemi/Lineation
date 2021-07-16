package com.diademiemi.lineation.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.diademiemi.lineation.line.Line;

/**
 * class for tab complete functionality on lineation commands
 *
 * @author diademiemi
 */
public class CommandTabComplete implements TabCompleter {

    /**
     * method to implement tab completion
     *
     * @param sender command sender
     * @param command command
     * @param label command label 
     * @param args command args
     * @return tab list
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        ArrayList<String> tabList = new ArrayList<>();

        Set<String> lines = Line.getLines().keySet();

        if (sender instanceof Player && (label.equalsIgnoreCase("lineation") || label.equalsIgnoreCase("la"))) {

            Player player = (Player) sender;

            if (args.length == 0 || args.length == 1) {
                if (args[0].equalsIgnoreCase("")) {

                    tabList.add("line");
                    if (player.hasPermission("lineation.help")) tabList.add("help");
                    if (player.hasPermission("lineation.reload")) tabList.add("reload");
                } else if (args[0].startsWith("l")) tabList.add("line");
                else if (args[0].startsWith("h") && player.hasPermission("lineation.help")) tabList.add("help");
                else if (args[0].startsWith("r") && player.hasPermission("lineation.help")) tabList.add("reload");
            
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("help")) {
                    if (args[1].equalsIgnoreCase("")) {
                        if (player.hasPermission("lineation.help")) {
                            tabList.add("lines");
                            tabList.add("options");
                        }
                    } else if (args[1].startsWith("l") && player.hasPermission("lineation.help")) tabList.add("lines");
                    else if (args[1].startsWith("o") && player.hasPermission("lineation.help")) tabList.add("options");

                } else if (args[0].equalsIgnoreCase("line")) {

                    if (args[1].equalsIgnoreCase("")) {
                        
                        if (player.hasPermission("lineation.line.create")) tabList.add("create");
                        if (player.hasPermission("lineation.line.remove")) tabList.add("remove");
                        if (player.hasPermission("lineation.line.list")) tabList.add("list");
                        
                    } else if (args[1].startsWith("c") && player.hasPermission("lineation.line.create")) tabList.add("create");
                    else if (args[1].startsWith("r") && player.hasPermission("lineation.line.remove")) tabList.add("remove");
                    else if (args[1].startsWith("l") && player.hasPermission("lineation.line.list")) tabList.add("list");

                    if (player.hasPermission("lineation.line.list")) {
                        for (String line : lines) {
                            if (line.startsWith(args[1])) {
                                tabList.add(line);
                            }
                        }
                    }
                }

            } else if (args.length == 3) {
                if (lines.contains(args[1])) {
                    if (args[2].equalsIgnoreCase("")) {
                        if (player.hasPermission("lineation.line.info")) tabList.add("info");
                        if (player.hasPermission("lineation.line.start")) tabList.add("start");
                        if (player.hasPermission("lineation.line.stop")) tabList.add("setarea");
                    } else if (args[2].startsWith("i") && player.hasPermission("lineation.line.info")) tabList.add("info");
                    else if ("start".startsWith(args[2]) && player.hasPermission("lineation.line.start")) tabList.add("start");
                    else if ("stop".startsWith(args[2]) && player.hasPermission("lineation.line.stop")) tabList.add("stop");
                    else if (args[2].startsWith("o") && player.hasPermission("lineation.line.list")) tabList.add("option");
                }
            }
        
            return tabList;

        }

        return null;
    }

}
