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
 * Class for tab complete functionality on lineation commands
 *
 * @author diademiemi
 */
public class CommandTabComplete implements TabCompleter {

    /**
     * Method to implement tab completion
     *
     * @param sender    Entity sending the command
     * @param command   Command
     * @param label Command label used
     * @param args  List of arguments
     * @return tab list
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        ArrayList<String> tabList = new ArrayList<>();

        Set<String> lines = Line.getLines().keySet();
        Set<String> startLines = Line.getStartLines().keySet();
        Set<String> finishLines = Line.getFinishLines().keySet();

        if (sender instanceof Player && (label.equalsIgnoreCase("lineation") || label.equalsIgnoreCase("ln"))) {

            Player player = (Player) sender;

            if (args.length == 0 || args.length == 1) {

                if (args[0].equalsIgnoreCase("")) {

                    tabList.add("line");
                    if (player.hasPermission("lineation.help")) tabList.add("help");
                    if (player.hasPermission("lineation.config")) tabList.add("config");
                }

                if ("line".startsWith(args[0])) tabList.add("line");
                if ("help".startsWith(args[0]) && player.hasPermission("lineation.help")) tabList.add("help");
                if ("config".startsWith(args[0]) && player.hasPermission("lineation.help")) tabList.add("config");
            
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

                } else if (args[0].equalsIgnoreCase("config")) {
                    
                    if (args[1].equalsIgnoreCase("")) {
                        
                        if (player.hasPermission("lineation.reload")) tabList.add("reload");
                        if (player.hasPermission("lineation.maxwins")) tabList.add("maxwins");
                        if (player.hasPermission("lineation.forget")) tabList.add("forget");

                    }

                    if ("reload".startsWith(args[1]) && player.hasPermission("lineation.reload")) tabList.add("reload");
                    if ("mawins".startsWith(args[1]) && player.hasPermission("lineation.maxwins")) tabList.add("maxwins");
                    if ("forget".startsWith(args[1]) && player.hasPermission("lineation.forget")) tabList.add("forget");

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
                        if (player.hasPermission("lineation.line.addcheckpoint") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) tabList.add("addcheckpoint");
                        if (player.hasPermission("lineation.line.removecheckpoint") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) tabList.add("removecheckpoint");
                        if (player.hasPermission("lineation.line.setarea")) tabList.add("setarea");
                        if (player.hasPermission("lineation.line.getwinners") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) tabList.add("getwinners");
                        if (player.hasPermission("lineation.line.list")) tabList.add("option");

                    } 

                    if ("info".startsWith(args[2]) && player.hasPermission("lineation.line.info")) tabList.add("info");
                    if ("start".startsWith(args[2]) && player.hasPermission("lineation.line.start")) tabList.add("start");
                    if ("stop".startsWith(args[2]) && player.hasPermission("lineation.line.stop")) tabList.add("stop");
                    if ("setarea".startsWith(args[2]) && player.hasPermission("lineation.line.setarea")) tabList.add("setarea");
                    if ("addborder".startsWith(args[2]) && player.hasPermission("lineation.line.addborder")) tabList.add("addborder");
                    if ("removeborder".startsWith(args[2]) && player.hasPermission("lineation.line.removeborder")) tabList.add("removeborder");
                    if ("addcheckpoint".startsWith(args[2]) && player.hasPermission("lineation.line.addcheckpoint") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) tabList.add("addcheckpoint");
                    if ("removecheckpoint".startsWith(args[2]) && player.hasPermission("lineation.line.removecheckpoint") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) tabList.add("removecheckpoint");
                    if ("getwinners".startsWith(args[2]) && player.hasPermission("lineation.line.getwinners") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) tabList.add("getwinners");
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
                                tabList.add(line);
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
                        if (player.hasPermission("lineation.line.list")) tabList.add("started");
                    }

                    if ("start".startsWith(args[2]) && player.hasPermission("lineation.line.list")) tabList.add("start");
                    if ("finish".startsWith(args[2]) && player.hasPermission("lineation.line.list")) tabList.add("finish");
                    if ("started".startsWith(args[2]) && player.hasPermission("lineation.line.list")) tabList.add("started");

                }

            } else if (args.length == 4) {

                if (lines.contains(args[1])) {

                    if (args[2].equalsIgnoreCase("option")) {

                        if (args[3].equalsIgnoreCase("")) {

                            if (player.hasPermission("lineation.line.option.messagereach") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) tabList.add("messagereach");
                            if (player.hasPermission("lineation.line.option.maxwinners") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) tabList.add("maxwinners");
                            if (player.hasPermission("lineation.line.option.gamemodes") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) tabList.add("gamemodes");
                            if (player.hasPermission("lineation.line.option.blocksequence")) tabList.add("blocksequence");
                            if (player.hasPermission("lineation.line.option.teleport")) tabList.add("teleport");
                            if (player.hasPermission("lineation.line.option.laps") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) tabList.add("laps");
                            if (player.hasPermission("lineation.line.option.link")) tabList.add("link");
                            
                        }

                        if ("messagereach".startsWith(args[3]) && player.hasPermission("lineation.line.option.messagereach") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) tabList.add("messagereach");
                        if ("maxwins".startsWith(args[3]) && player.hasPermission("lineation.line.option.maxwinners") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) tabList.add("maxwinners");
                        if ("gamemodes".startsWith(args[3]) && player.hasPermission("lineation.line.option.gamemodes") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) tabList.add("gamemodes");
                        if ("blocksequence".startsWith(args[3]) && player.hasPermission("lineation.line.option.blocksequence")) tabList.add("blocksequence");
                        if ("teleport".startsWith(args[3]) && player.hasPermission("lineation.line.option.teleport")) tabList.add("teleport");
                        if ("laps".startsWith(args[3]) && player.hasPermission("lineation.line.option.laps") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) tabList.add("laps");
                        if ("link".startsWith(args[3]) && player.hasPermission("lineation.line.option.link")) tabList.add("link");

                    } else if (args[2].equalsIgnoreCase("info")) {

                        if (args[3].equalsIgnoreCase("")) {

                            if (player.hasPermission("lineation.line.info.options")) tabList.add("options");

                        }

                        if ("options".startsWith(args[3]) && player.hasPermission("lineation.line.info.options")) tabList.add("options");

                    } else if (args[2].equalsIgnoreCase("removeborder")) {

                        if (args[3].equalsIgnoreCase("")) {

                            if (player.hasPermission("lineation.line.removeborder")) {
                                int i = 1;
                                int b = Line.getLines().get(args[1]).getBorders().size();

                                while (i <= b) {
                                    tabList.add(String.valueOf(i));
                                    i++;
                                }

                            }

                        }

                    } else if (args[2].equalsIgnoreCase("removecheckpoint") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) {

                        if (args[3].equalsIgnoreCase("")) {

                            if (player.hasPermission("lineation.line.removecheckpoint")) {
                                int i = 1;
                                int c = Line.getLines().get(args[1]).getCheckpoints().size();

                                while (i <= c) {
                                    tabList.add(String.valueOf(i));
                                    i++;
                                }

                            }

                        }

                    }

                }

            } else if (args.length == 5) {

                if (lines.contains(args[1])) {
                    
                    if (args[2].equalsIgnoreCase("option")) {

                        if (args[3].equalsIgnoreCase("teleport") && player.hasPermission("lineation.line.option.teleport")) {

                            if (args[4].equalsIgnoreCase("")) {

                                tabList.add("here");
                                tabList.add("disabled");

                            }

                            if ("here".startsWith(args[4])) tabList.add("here");
                            if ("disabled".startsWith(args[4])) tabList.add("disabled");

                        } else if (args[3].equalsIgnoreCase("link") && player.hasPermission("lineation.line.option.link") && player.hasPermission("lineation.line.list")) {

                            if (args[4].equalsIgnoreCase("")) {

                                if (Line.getLines().get(args[1]).getType().equalsIgnoreCase("start")) {
                                    
                                    for (String line : finishLines) {
                                        tabList.add(line);
                                    }

                                } else if (Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) {

                                    for (String line : startLines) {
                                        tabList.add(line);
                                    }

                                }

                            }

                            if (Line.getLines().get(args[1]).getType().equalsIgnoreCase("start")) {
                                
                                for (String line : finishLines) {
                                    if (line.startsWith(args[4])) tabList.add(line);
                                }

                            } else if (Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) {

                                for (String line : startLines) {
                                    if (line.startsWith(args[4])) tabList.add(line);
                                }

                            }

                        }
                    }
                }
            }

            return tabList;

        }

        return null;

    }

}
