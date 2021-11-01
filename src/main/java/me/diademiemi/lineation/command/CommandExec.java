package me.diademiemi.lineation.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.diademiemi.lineation.Lineation;
import me.diademiemi.lineation.Message;
import me.diademiemi.lineation.Config;
import me.diademiemi.lineation.line.Line;
import me.diademiemi.lineation.line.LineIO;
import me.diademiemi.lineation.line.LineTools;

import java.util.Arrays;

/**
 * Command class for listening for lineation command
 *
 * @author diademiemi
 */
public class CommandExec implements CommandExecutor {

    /**
     * Method to handle commands
     *
     * @param sender    Entity sending the command
     * @param command   Command
     * @param label Command label used
     * @param args  List of arguments
     * @return  Boolean of if command was successful
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("lineation") || label.equalsIgnoreCase("ln")) {
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "help":
                        if (args.length > 1) {
                            switch (args[1].toLowerCase()) {
                                case "lines":
                                    if (sender.hasPermission("lineation.help")) {
                                        sender.sendMessage(Message.HELP_LINE);
                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                    break;
                                case "options":
                                    if (sender.hasPermission("lineation.help")) {
                                        sender.sendMessage(Message.HELP_OPTION);
                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                    break;
                            }
                        } else if (sender.hasPermission("lineation.help")) {
                            sender.sendMessage(Message.HELP_INDEX);
                        } else sender.sendMessage(Message.ERROR_NO_PERMS);
                        break;
                    case "config":
                        if (args.length > 1) {
                            switch (args[1].toLowerCase()) {
                                case "reload":
                                    if (sender.hasPermission("lineation.reload")) {
                                        LineIO.saveAll();
                                        Config.getPluginConfig().saveConfig();
                                        Config.getData().saveConfig();
                                        Config.getLineConfig().reloadConfig();
                                        Config.getPluginConfig().reloadConfig();
                                        Config.getData().reloadConfig();
                                        Config.getMessageConfig().reloadConfig();
                                        LineIO.loadAll();
                                        sender.sendMessage(Message.SUCCESS_RELOAD);
                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                    break;
                                case "maxwins":
                                    if (sender.hasPermission("lineation.maxwins")) {
                                        try {
                                            int i = Integer.parseInt(args[2]);
                                            Config.getPluginConfig().getConfig().set("maxwins", i);
                                            sender.sendMessage(Message.SUCCESS_OPTION_SET);
                                        } catch (Exception e) {
                                            sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                        }
                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                    break;
                                case "forget":
                                    if (sender.hasPermission("lineation.forget")) {
                                        try {
                                            if (Config.getData().getConfig().isInt(args[2])) {
                                                Config.getData().getConfig().set(args[2], null);
                                                sender.sendMessage(Message.SUCCESS_PLAYER_FORGOTTEN.replace("$PLAYER$", args[2]));
                                            } else if (args[2].equals("all")) {
                                                Lineation.getInstance().saveResource("data.yml", true);
                                                Config.getData().reloadConfig();
                                                sender.sendMessage(Message.SUCCESS_PLAYER_FORGOTTEN.replace("$PLAYER$", args[2]));
                                            } else if (Config.getData().getConfig().isInt(Bukkit.getPlayer(args[2]).getUniqueId().toString())) {
                                                Config.getData().getConfig().set(Bukkit.getPlayer(args[2]).getUniqueId().toString(), null);
                                                sender.sendMessage(Message.SUCCESS_PLAYER_FORGOTTEN.replace("$PLAYER$", args[2]));
                                        } else sender.sendMessage(Message.ERROR_UNKNOWN_PLAYER.replace("$PLAYER$", args[2]));
                                        } catch (Exception e) {
                                            sender.sendMessage(Message.ERROR_UNKNOWN_PLAYER.replace("$PLAYER$", args[2]));
                                        }
                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                    break;
                                default:
                                    sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                            }
                            break;
                        } sender.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help"));
                        break;
                    case "line": 
                        if (args.length > 1) {
                            switch (args[1].toLowerCase()) {
                                case "create":
                                    if (args.length > 2) {
                                        switch (args[2].toLowerCase()) {
                                            case "start":
                                            case "finish":
                                                if (sender.hasPermission("lineation.line.create")) {
                                                    if (args.length > 3) {
                                                        if (Line.getLines().get(args[3]) == null) {
                                                            if (!args[3].equalsIgnoreCase("help")
                                                                && !args[3].equalsIgnoreCase("create")
                                                                && !args[3].equalsIgnoreCase("remove")
                                                                && !args[3].equalsIgnoreCase("list")) {
                                                                new Line(args[3], args[2].toLowerCase());
                                                                sender.sendMessage(Message.SUCCESS_LINE_CREATED.replace("$LINE$", args[3]));
                                                            } else sender.sendMessage(Message.ERROR_INVALID_NAME);
                                                        } else sender.sendMessage(Message.ERROR_LINE_EXISTS);
                                                    } else sender.sendMessage(Message.ERROR_MISSING_ARGS.replace("$MISSING$", "<name>"));
                                                    break;
                                                } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                break;
                                            default:
                                                sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                break;
                                        } break;
                                    }
                                    else sender.sendMessage(Message.ERROR_MISSING_ARGS.replace("$MISSING$", "<type>, <name>"));
                                    break;
                                case "list":
                                    if (args.length > 2) {
                                        switch (args[2].toLowerCase()) {
                                            case "start":
                                                if (sender.hasPermission("lineation.line.list")) {
                                                    sender.sendMessage(Message.LINE_LIST.replace("$LINES$",
                                                                Line.getStartLines().keySet().toString()
                                                                    .replace("[", "").replace("]", "")));
                                                } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                break;
                                            case "finish":
                                                if (sender.hasPermission("lineation.line.list")) {
                                                    sender.sendMessage(Message.LINE_LIST.replace("$LINES$",
                                                                Line.getFinishLines().keySet().toString()
                                                                    .replace("[", "").replace("]", "")));
                                                } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                break;
                                            case "started":
                                                if (sender.hasPermission("lineation.line.list")) {
                                                    sender.sendMessage(Message.LINE_LIST.replace("$LINES$",
                                                                Line.getStartedLines().keySet().toString()
                                                                    .replace("[", "").replace("]", "")));
                                                } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                break;
                                            default:
                                                sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                break;
                                        }
                                    } else if (sender.hasPermission("lineation.line.list")) {
                                                    sender.sendMessage(Message.LINE_LIST.replace("$LINES$",
                                                                Line.getLines().keySet().toString()
                                                                    .replace("[", "").replace("]", "")));
                                        } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                    break;
                                case "remove":
                                    if (sender.hasPermission("lineation.line.remove")) {
                                        if (args.length > 2 && Line.getLines().get(args[2]) != null) {
                                            if (Line.getLines().get(args[2]).getLinkedLine() != "") {
                                                Line.getLines().remove(Line.getLines().get(args[2]).getLinkedLine());
                                            }
                                            Line.getLines().remove(args[2]);
                                            sender.sendMessage(Message.SUCCESS_LINE_REMOVED
                                                    .replace("$LINE$", args[2]));
                                        } else if (sender.hasPermission("lineation.line.list")) {
                                            sender.sendMessage(Message.ERROR_UNKNOWN_LINE.replace("$LINE$", args[2]));
                                        } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                    break;
                                default:
                                    if (Line.getLines().get(args[1]) != null) {
                                        if (args.length > 2) {
                                            Line line = Line.getLines().get(args[1]);
                                            switch (args[2].toLowerCase()) {
                                                case "info":
                                                    if (sender.hasPermission("lineation.line.info")) {
                                                        if (args.length > 3) {
                                                            if (args[3].equalsIgnoreCase("options")) {
                                                                LineTools.getLineOptions(line, sender);
                                                                break;
                                                            } else sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                        } LineTools.getLineInfo(line, sender);
                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                    break;
                                                case "getwinners":
                                                    if (sender.hasPermission("lineation.line.getwinners")) {
                                                        if (line.getType().equalsIgnoreCase("finish")) {
                                                            LineTools.getWinnersString(line, sender);
                                                        } else sender.sendMessage(Message.ERROR_NOT_FINISH.replace("$LINE$", args[1]));
                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                    break;
                                                case "setarea":
                                                    if (sender.hasPermission("lineation.line.setarea") && sender instanceof Player) {
                                                        line.setArea((Player) sender);
                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                    break;
                                                case "addborder":
                                                    if (sender.hasPermission("lineation.line.addborder") && sender instanceof Player) {
                                                        line.addBorder((Player) sender);
                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                    break;
                                                case "removeborder":
                                                    if (sender.hasPermission("lineation.line.removeborder")) {
                                                        if (args.length > 3) {
                                                            switch (args[3].toLowerCase()) {
                                                                case "all":
                                                                    line.clearBorders();
                                                                    sender.sendMessage(Message.SUCCESS_BORDER_REMOVED);
                                                                    break;
                                                                default:
                                                                    try { 
                                                                        int i = Integer.parseInt(args[3]);
                                                                        line.removeBorder(i);
                                                                        sender.sendMessage(Message.SUCCESS_BORDER_REMOVED);
                                                                    } catch (Exception e) {
                                                                        sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                                    }
                                                                    break;
                                                            }
                                                            break;
                                                        } else sender.sendMessage(Message.ERROR_MISSING_ARGS.replace("$MISSING$", "<all/nunber>"));
                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                    break;
                                                case "addcheckpoint":
                                                    if (sender.hasPermission("lineation.line.addcheckpoint") && sender instanceof Player) {
                                                        if (line.getType().equalsIgnoreCase("finish")) {
                                                            line.addCheckpoint((Player) sender);
                                                        } else sender.sendMessage(Message.ERROR_NOT_FINISH.replace("$LINE$", args[1]));
                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                    break;
                                                case "removecheckpoint":
                                                    if (sender.hasPermission("lineation.line.removecheckpoint")) {
                                                        if (line.getType().equalsIgnoreCase("finish")) {
                                                            if (args.length > 3) {
                                                                switch (args[3].toLowerCase()) {
                                                                    case "all":
                                                                        line.clearCheckpoints();
                                                                        sender.sendMessage(Message.SUCCESS_CHECKPOINT_REMOVED);
                                                                        if (line.getCheckpoints().size() == 0) line.setLaps(1);
                                                                        break;
                                                                    default:
                                                                        try {
                                                                            int i = Integer.parseInt(args[3]);
                                                                            line.removeCheckpoint(i);
                                                                            sender.sendMessage(Message.SUCCESS_CHECKPOINT_REMOVED);
                                                                            if (line.getCheckpoints().size() == 0) line.setLaps(1);
                                                                        } catch (Exception e) {
                                                                            sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                                        }
                                                                        break;
                                                                }
                                                                break;
                                                            } else sender.sendMessage(Message.ERROR_MISSING_ARGS.replace("$MISSING$", "<all/nunber>"));
                                                        } else sender.sendMessage(Message.ERROR_NOT_FINISH.replace("$LINE$", args[1]));
                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                    break;
                                                case "start":
                                                    if (sender.hasPermission("lineation.line.start")) {
                                                        LineTools.startLine(line);
                                                        sender.sendMessage(Message.SUCCESS_LINE_STARTED
                                                                .replace("$LINE$", args[1]));
                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                    break;
                                                case "stop":
                                                    if (sender.hasPermission("lineation.line.stop")) {
                                                        LineTools.stopLine(line);
                                                        sender.sendMessage(Message.SUCCESS_LINE_STOPPED
                                                                .replace("$LINE$", args[1]));
                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                    break;
                                                case "option":
                                                    if (args.length > 3) {
                                                        switch (args[3].toLowerCase()) {
                                                            case "maxwinners":
                                                                if (args.length > 4) {
                                                                    if (sender.hasPermission("lineation.line.option.maxwinners")) {
                                                                        if (line.getType().equalsIgnoreCase("finish")) {
                                                                            try {
                                                                                int i = Integer.parseInt(args[4]);
                                                                                line.setMaxWinners(i);
                                                                                sender.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                            } catch (Exception e) {
                                                                                sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                                            }
                                                                            break;
                                                                        } else sender.sendMessage(Message.ERROR_NOT_FINISH.replace("$LINE$", args[1]));
                                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                                } sender.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                                break;
                                                            case "addcommand":
                                                                if (args.length > 4) {
                                                                    if (sender.hasPermission("lineation.line.option.addcommand")) {
                                                                        if (line.getType().equalsIgnoreCase("finish")) {
                                                                            StringBuilder com = new StringBuilder("");
                                                                            for (int i = 4; i < args.length; i++) {
                                                                                com.append(args[i]);
                                                                                if (i + 1 != args.length) {
                                                                                    com.append(" ");
                                                                                }
                                                                            }
                                                                            line.addCommand(com.toString());
                                                                            sender.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                        } else sender.sendMessage(Message.ERROR_NOT_FINISH.replace("$LINE$", args[1]));
                                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                                    break;
                                                                } sender.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                                break;
                                                            case "removecommand":
                                                                if (args.length > 4) {
                                                                    if (sender.hasPermission("lineation.line.option.removecommand")) {
                                                                        if (line.getType().equalsIgnoreCase("finish")) {
                                                                            switch (args[4].toLowerCase()) {
                                                                                case "all":
                                                                                    line.clearCommands();
                                                                                    sender.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                                    break;
                                                                                default: 
                                                                                    try {
                                                                                        int i = Integer.parseInt(args[4]);
                                                                                        line.removeCommand(i);
                                                                                        sender.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                                    } catch (Exception e) {
                                                                                        sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                                                    }
                                                                                    break;
                                                                            }
                                                                            break;
                                                                        } else sender.sendMessage(Message.ERROR_NOT_FINISH.replace("$LINE$", args[1]));
                                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                                    break;
                                                                } sender.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                                break;
                                                            case "messagereach":
                                                                if (args.length > 4) {
                                                                    switch (args[4].toLowerCase()) {
                                                                        case "world":
                                                                        case "disabled":
                                                                        case "all":
                                                                            if (sender.hasPermission("lineation.line.option.messagereach")) {
                                                                                if (line.getType().equalsIgnoreCase("finish")) {
                                                                                    line.setMessageReach(args[4]);
                                                                                    sender.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                                } else sender.sendMessage(Message.ERROR_NOT_FINISH.replace("$LINE$", args[1]));
                                                                            } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                                            break;
                                                                        default:
                                                                            sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                                    }
                                                                    break;
                                                                } sender.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                                break;
                                                            case "gamemodes":
                                                                if (args.length > 4) {
                                                                    if (sender.hasPermission("lineation.line.option.gamemodes")) {
                                                                        if (line.getType().equalsIgnoreCase("finish")) {
                                                                            try {
																				if (line.setGameModes(String.join(",", Arrays.copyOfRange(args, 4, args.length)).replaceAll("\\s", "").toUpperCase())) {
																					sender.sendMessage(Message.SUCCESS_OPTION_SET);
																				} else {
																					sender.sendMessage(Message.ERROR_INVALID_GAMEMODE);
																				}
                                                                            } catch (Exception e) {
                                                                                sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                                            }
                                                                        } else sender.sendMessage(Message.ERROR_NOT_FINISH.replace("$LINE$", args[1]));
                                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                                    break;
                                                                } sender.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                                break;
                                                            case "blocksequence":
                                                                if (args.length > 4) {
                                                                    if (sender.hasPermission("lineation.line.option.blocksequence")) {
                                                                        try {
                                                                            if (line.setBlockSequence(String.join(",", Arrays.copyOfRange(args, 4, args.length)).replaceAll("\\s", "").toLowerCase())) {
																				sender.sendMessage(Message.SUCCESS_OPTION_SET);
																			} else {
																				sender.sendMessage(Message.ERROR_INVALID_BLOCK);
																			}
                                                                        } catch (Exception e) {
                                                                            sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                                        }
                                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                                    break;
                                                                } sender.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                                break;
                                                            case "teleport":
                                                                if (args.length > 4) {
                                                                    if (sender.hasPermission("lineation.line.option.teleport")) {
                                                                        switch (args[4].toLowerCase()) {
                                                                            case "here":
                                                                                if (sender instanceof Player) {
                                                                                    line.setTeleportLocation((Player) sender);
                                                                                    line.setTeleportEnabled(true);
                                                                                    sender.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                                } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                                                break;
                                                                            case "disable":
                                                                                line.setTeleportEnabled(false);
                                                                                sender.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                                break;
                                                                            default:
                                                                            sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                                        }
                                                                        break;
                                                                    } sender.sendMessage(Message.ERROR_NO_PERMS);
                                                                    break;
                                                                } sender.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                                break;
                                                            case "laps":
                                                                if (args.length > 4) {
                                                                    if (sender.hasPermission("lineation.line.option.laps")) {
                                                                        if (line.getType().equalsIgnoreCase("finish")) {
                                                                            if (line.getCheckpoints().size() != 0) {
                                                                                try {
                                                                                    int i = Integer.parseInt(args[4]);
                                                                                    line.setLaps(i);
                                                                                    sender.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                                } catch (Exception e) {
                                                                                    sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                                                }
                                                                                break;
                                                                            } else sender.sendMessage(Message.ERROR_NO_CHECKPOINT.replace("$LINE$", args[1]));
                                                                            break;
                                                                        } else sender.sendMessage(Message.ERROR_NOT_FINISH.replace("$LINE$", args[1]));
                                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                                } sender.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                                break;
                                                            case "link":
                                                                if (args.length > 4) {
                                                                    if (sender.hasPermission("lineation.line.option.link")) {
                                                                        if (Line.getLines().get(args[4]) != null) {
                                                                            Line linkLine = Line.getLines().get(args[4]);
                                                                            if (line.getType().equalsIgnoreCase("start")) {
                                                                                if (linkLine.getType().equalsIgnoreCase("finish")) {
                                                                                    line.setLinkedLine(args[4]);
                                                                                    linkLine.setLinkedLine(args[1]);
                                                                                    sender.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                                    break;
                                                                                } sender.sendMessage(Message.ERROR_NOT_FINISH.replace("$LINE$", args[4]));
                                                                            } else if (line.getType().equalsIgnoreCase("finish")) {
                                                                                if (linkLine.getType().equalsIgnoreCase("start")) {
                                                                                    line.setLinkedLine(args[4]);
                                                                                    linkLine.setLinkedLine(args[1]);
                                                                                    sender.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                                    break;
                                                                                } sender.sendMessage(Message.ERROR_NOT_START.replace("$LINE$", args[4]));
                                                                            }
                                                                        } else sender.sendMessage(Message.ERROR_UNKNOWN_LINE.replace("$LINE$", args[4]));
                                                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                                                } else sender.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                                break;
                                                            default:
                                                                sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                        }
                                                        break;
                                                    } else sender.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                    break;
                                                default:
                                                    sender.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help lines"));
                                            }
                                        } else sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                    } else if (sender.hasPermission("lineation.line.list")) {
                                        sender.sendMessage(Message.ERROR_UNKNOWN_LINE.replace("$LINE$", args[2]));
                                    } else sender.sendMessage(Message.ERROR_NO_PERMS);
                                    break;
                            }
                        } else if (sender.hasPermission("lineation.help")) {
                                    sender.sendMessage(Message.HELP_LINE);
                                 } else sender.sendMessage(Message.ERROR_NO_PERMS);
                    break;
                    default:
                        sender.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                        break;
                    }
                } else if (sender.hasPermission("lineation.help")) sender.sendMessage(Message.HELP_INDEX);
                    else sender.sendMessage(Message.ERROR_NO_PERMS);
            }
        return true;
    }
}
