package me.diademiemi.lineation.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.diademiemi.lineation.line.Line;

/**
 * Class for tab complete functionality on lineation commands
 *
 * @author diademiemi
 */
public class CommandTabComplete implements TabCompleter {

	/**
	 * Private boolean to determine whether this option should get completed
	 *
	 * @param option	Option to check
	 * @param current	Currently typed
	 *
	 * @return	Boolean of whether to complete
	 */
	private void shouldTab(String option, String current) {
		if (current.equals("")) {
			tabList.add(option);
		} else if (option.startsWith(current.toLowerCase())) {
			tabList.add(option);
		}
	}

	/**
	 * Private boolean to determine whether this option should get completed
	 *
	 * @param option	Option to check
	 * @param current	Currently typed
	 * @param player	Player to check for
	 * @param permission	String of the permission required
	 *
	 * @return	Boolean of whether to complete
	 */
	private void shouldTab(String option, String current, Player player, String permission) {
		if (player.hasPermission(permission)) {
			if (current.equals("")) {
				tabList.add(option);
			} else if (option.startsWith(current.toLowerCase())) {
				tabList.add(option);
			} 
		}
	}

	public ArrayList<String> tabList;

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
		
		tabList = new ArrayList<>();

        Set<String> lines = Line.getLines().keySet();
        Set<String> startLines = Line.getStartLines().keySet();
        Set<String> finishLines = Line.getFinishLines().keySet();

        if (sender instanceof Player && (label.equalsIgnoreCase("lineation") || label.equalsIgnoreCase("ln"))) {

            Player player = (Player) sender;

            if (args.length == 0 || args.length == 1) {

				shouldTab("line", args[0]);
				shouldTab("config", args[0], player, "lineation.config");
				shouldTab("help", args[0], player, "lineation.help");
            
            } else if (args.length == 2) {

                if (args[0].equalsIgnoreCase("help") && player.hasPermission("lineation.help")) {

					shouldTab("lines", args[1]);
					shouldTab("start", args[1]);
					shouldTab("finish", args[1]);
					shouldTab("options", args[1]);
					shouldTab("areas", args[1]);
					shouldTab("borders", args[1]);

                } else if (args[0].equalsIgnoreCase("config")) {
                    
					shouldTab("reload", args[1], player, "lineation.config.reload");
					shouldTab("maxwins", args[1], player, "lineation.config.maxwins");
					shouldTab("forget", args[1], player, "lineation.config.forget");

                } else if (args[0].equalsIgnoreCase("line")) {

					shouldTab("create", args[1], player, "lineation.line.create");
					shouldTab("remove", args[1], player, "lineation.line.remove");
					shouldTab("list", args[1], player, "lineation.line.list");
					shouldTab("here", args[1], player, "lineation.line.here");

                    if (player.hasPermission("lineation.line.list")) {
                        for (String line : lines) {
							shouldTab(line, args[1]);
                        }
                    }
                }

            } else if (args.length == 3) {

                if (lines.contains(args[1])) {

					shouldTab("info", args[2], player, "lineation.line.info");
					shouldTab("start", args[2], player, "lineation.line.start");
					shouldTab("stop", args[2], player, "lineation.line.stop");
					shouldTab("setarea", args[2], player, "lineation.line.setarea");
					shouldTab("addborder", args[2], player, "lineation.line.setarea");
					shouldTab("removeborder", args[2], player, "lineation.line.removeborder");
					shouldTab("link", args[2], player, "lineation.line.link");
					shouldTab("tp", args[2], player, "lineation.line.tp");
					shouldTab("option", args[2], player, "lineation.line.list"); // Require a permission so line list cant be brute forced

					if (Line.getLines().get(args[1]).getType().equals("finish")) {

						shouldTab("addcheckpoint", args[2], player, "lineation.line.addcheckpoint");
						shouldTab("removecheckpoint", args[2], player, "lineation.line.removecheckpoint");
						shouldTab("getwinners", args[2], player, "lineation.line.getwinners");

					}

                } else if (args[1].equalsIgnoreCase("create")) {

					shouldTab("start", args[2], player, "lineation.line.create");
					shouldTab("finish", args[2], player, "lineation.line.create");
                
                } else if (args[1].equalsIgnoreCase("remove")) {

                    if (player.hasPermission("lineation.line.remove") && player.hasPermission("lineation.line.list")) {

                        for (String line : lines) {
							shouldTab(line, args[2]);
                        }

                    }

                } else if (args[1].equalsIgnoreCase("list")) {

					if (player.hasPermission("lineation.line.list")) {

						shouldTab("start", args[2]);
						shouldTab("finish", args[2]);
						shouldTab("started", args[2]);
					
					}

                }

            } else if (args.length == 4) {

                if (lines.contains(args[1])) {

                    if (args[2].equalsIgnoreCase("option")) {

						shouldTab("messagereach", args[3], player, "lineation.line.option.messagereach");
						shouldTab("gamemodes", args[3], player, "lineation.line.option.gamemodes");
						shouldTab("teleport", args[3], player, "lineation.line.option.teleport");

						if (Line.getLines().get(args[1]).getType().equals("start")) {

							shouldTab("blocksequence", args[3], player, "lineation.line.option.blocksequence");
							shouldTab("illegalarea", args[3], player, "lineation.line.option.illegalarea");

						} else if (Line.getLines().get(args[1]).getType().equals("finish")) {

							shouldTab("block", args[3], player, "lineation.line.option.blocksequence");
							shouldTab("laps", args[3], player, "lineation.line.option.laps");
							shouldTab("maxwins", args[3], player, "lineation.line.option.maxwins");
							
						}

                    } else if (args[2].equalsIgnoreCase("info")) {

						shouldTab("options", args[3], player, "lineation.line.info.options");

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

					} else if (args[2].equalsIgnoreCase("link") && player.hasPermission("lineation.line.option.link") && player.hasPermission("lineation.line.list")) {

						if (Line.getLines().get(args[1]).getType().equalsIgnoreCase("start")) {
							
							for (String line : finishLines) {
								shouldTab(line, args[3]);
							}

						} else if (Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) {

							for (String line : startLines) {
								shouldTab(line, args[3]);
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

						if (args[3].equalsIgnoreCase("messagereach") && player.hasPermission("lineation.line.option.messagereach")) {

							if (args[4].equalsIgnoreCase("")) tabList.add("100");

							shouldTab("area", args[4]);
							shouldTab("all", args[4]);
							shouldTab("world", args[4]);
							shouldTab("disabled", args[4]);

						} else if (args[3].equalsIgnoreCase("teleport") && player.hasPermission("lineation.line.option.teleport")) {

							shouldTab("setlocation", args[4]);

							if (Line.getLines().get(args[1]).getType().equalsIgnoreCase("start")) {
								
								shouldTab("onstart", args[4]);
								shouldTab("illegalarea", args[4]);
								shouldTab("gamemodes", args[4], player, "lineation.line.option.gamemodes");

							} else if (Line.getLines().get(args[1]).getType().equalsIgnoreCase("finish")) {
								shouldTab("onfinish", args[4]);
							}

                        } else if (args[3].equalsIgnoreCase("illegalarea") && player.hasPermission("lineation.line.option.illegalarea") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("start")) {

							shouldTab("add", args[4], player, "lineation.line.option.illegalarea.add");
							shouldTab("remove", args[4], player, "lineation.line.option.illegalarea.remove");

						}

					}

				}

			} else if (args.length == 6) {

                if (lines.contains(args[1])) {
                    
                    if (args[2].equalsIgnoreCase("option")) {

                        if (args[3].equalsIgnoreCase("teleport") && player.hasPermission("lineation.line.option.teleport")) {

							switch (args[4].toLowerCase()) {
								case "illegalarea":
								case "onstart":
								case "onfinish":
									shouldTab("true", args[5]);
									shouldTab("false", args[5]);
									break;
								default:
									break;

                            }

						} else if (args[3].equalsIgnoreCase("illegalarea") && Line.getLines().get(args[1]).getType().equalsIgnoreCase("start")) {

							if (args[4].equalsIgnoreCase("remove") && player.hasPermission("lineation.line.option.illegalarea.remove")) {

								int i = 1;
								int c = Line.getLines().get(args[1]).getIllegalAreas().size();

								while (i <= c) {
									tabList.add(String.valueOf(i));
									i++;
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
