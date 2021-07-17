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
                }
                if ("line".startsWith(args[0])) tabList.add("line");
                if ("help".startsWith(args[0]) && player.hasPermission("lineation.help")) tabList.add("help");
                if ("reload".startsWith(args[0]) && player.hasPermission("lineation.help")) tabList.add("reload");
            
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("help")) {
                    if (args[1].equalsIgnoreCase("")) {
                        if (player.hasPermission("lineation.help")) {
                            tabList.add("lines");
                            tabList.add("options");
                        }
                    }
                    if ("lines".startsWith(args[1]) && player.hasPermission("lineation.help")) tabList.add("lines");
                    if ("options".startsWith(args[1]) && player.hasPermission("lineation.help")) tabList.add("options");

                } else if (args[0].equalsIgnoreCase("line")) {

                    if (args[1].equalsIgnoreCase("")) {
                        
                        if (player.hasPermission("lineation.line.create")) tabList.add("create");
                        if (player.hasPermission("lineation.line.remove")) tabList.add("remove");
                        if (player.hasPermission("lineation.line.list")) tabList.add("list");
                        
                    }
                    if ("create".startsWith(args[1]) && player.hasPermission("lineation.line.create")) tabList.add("create");
                    if ("remove".startsWith(args[1]) && player.hasPermission("lineation.line.remove")) tabList.add("remove");
                    if ("list".startsWith(args[1]) && player.hasPermission("lineation.line.list")) tabList.add("list");

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
                        if (player.hasPermission("lineation.line.stop")) tabList.add("stop");
                        if (player.hasPermission("lineation.line.addborder")) tabList.add("addborder");
                        if (player.hasPermission("lineation.line.removeborder")) tabList.add("removeborder");
                        if (player.hasPermission("lineation.line.setline")) tabList.add("setline");
                        if (player.hasPermission("lineation.line.getwinners")) tabList.add("getwinners");
                        if (player.hasPermission("lineation.line.list")) tabList.add("option");
                    } 
                    if ("info".startsWith(args[2]) && player.hasPermission("lineation.line.info")) tabList.add("info");
                    if ("start".startsWith(args[2]) && player.hasPermission("lineation.line.start")) tabList.add("start");
                    if ("stop".startsWith(args[2]) && player.hasPermission("lineation.line.stop")) tabList.add("stop");
                    if ("setarea".startsWith(args[2]) && player.hasPermission("lineation.line.setarea")) tabList.add("setarea");
                    if ("addborder".startsWith(args[2]) && player.hasPermission("lineation.line.addborder")) tabList.add("addborder");
                    if ("removeborder".startsWith(args[2]) && player.hasPermission("lineation.line.removeborder")) tabList.add("removeborder");
                    if ("getwinners".startsWith(args[2]) && player.hasPermission("lineation.line.getwinners")) tabList.add("getwinners");
                    if ("option".startsWith(args[2]) && player.hasPermission("lineation.line.list")) tabList.add("option"); 

                } else if (args[1].equalsIgnoreCase("create")) {
                    if (args[2].equalsIgnoreCase("")) {
                        if (player.hasPermission("lineation.line.create")) {
                            tabList.add("start");
                            tabList.add("finish");
                        }
                    }
                    if ("start".startsWith(args[2]) && player.hasPermission("lineation.line.create")) tabList.add("start");
                    if ("finish".startsWith(args[2]) && player.hasPermission("lineation.line.create")) tabList.add("finish");
                
                } else if (args[1].equalsIgnoreCase("remove")) {
                    if (args[2].equalsIgnoreCase("")) {
                        if (player.hasPermission("lineation.line.remove") && player.hasPermission("lineation.line.list")) {
                            for (String line : lines) {
                                if (line.startsWith(args[2])) {
                                    tabList.add(line);
                                }
                            }
                        }
                    }
                    if (player.hasPermission("lineation.line.remove") && player.hasPermission("lineation.line.list")) {
                        for (String line : lines) {
                            if (line.startsWith(args[2])) {
                                tabList.add(line);
                            }
                        }
                    }
                } else if (args[1].equalsIgnoreCase("list")) {
                    if (args[2].equalsIgnoreCase("")) {
                        if (player.hasPermission("lineation.line.list")) tabList.add("start");
                        if (player.hasPermission("lineation.line.list")) tabList.add("finish");
                    }
                    if ("start".startsWith(args[2]) && player.hasPermission("lineation.line.list")) tabList.add("start");
                    if ("finish".startsWith(args[2]) && player.hasPermission("lineation.line.list")) tabList.add("finish");
                }
            } else if (args.length == 4) {
                if (lines.contains(args[1])) {
                    if (args[2].equalsIgnoreCase("option")) {
                        if (args[3].equalsIgnoreCase("")) {
                            if (player.hasPermission("lineation.line.option.messagereach")) tabList.add("messagereach");
                            if (player.hasPermission("lineation.line.option.maxwins")) tabList.add("maxwins");
                        }
                        if ("messagereach".startsWith(args[3]) && player.hasPermission("lineation.line.option.messagereach")) tabList.add("messagereach");
                        if ("maxwins".startsWith(args[3]) && player.hasPermission("lineation.line.option.maxwins")) tabList.add("maxwins");

                    } else if (args[2].equalsIgnoreCase("info")) {
                        if (args[3].equalsIgnoreCase("")) {
                            if (player.hasPermission("lineation.line.info.options")) tabList.add("options");
                        }
                        if ("options".startsWith(args[3]) && player.hasPermission("lineation.line.info.options")) tabList.add("options");
                    }
                }
            }

            return tabList;

        }

        return null;

    }

}