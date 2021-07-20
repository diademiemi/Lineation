package com.diademiemi.lineation.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.diademiemi.lineation.Lineation;
import com.diademiemi.lineation.Message;
import com.diademiemi.lineation.Config;
import com.diademiemi.lineation.line.Line;
import com.diademiemi.lineation.line.LineIO;
import com.diademiemi.lineation.line.LineTools;

/**
 * Command class for listening for lineation command
 *
 * @author diademiemi
 */
public class CommandExec implements CommandExecutor {

    /**
     * Method to handle commands
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("lineation") || label.equalsIgnoreCase("ln")) {
                if (args.length > 0) {
                    switch (args[0].toLowerCase()) {
                        case "help":
                            if (args.length > 1) {
                                switch (args[1].toLowerCase()) {
                                    case "lines":
                                        if (player.hasPermission("lineation.help")) {
                                            player.sendMessage(Message.HELP_LINE);
                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                        break;
                                    case "options":
                                        if (player.hasPermission("lineation.help")) {
                                            player.sendMessage(Message.HELP_OPTION);
                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                        break;
                                }
                            } else if (player.hasPermission("lineation.help")) {
                                player.sendMessage(Message.HELP_INDEX);
                            } else player.sendMessage(Message.ERROR_NO_PERMS);
                            break;
                        case "config":
                            if (args.length > 1) {
                                switch (args[1].toLowerCase()) {
                                    case "reload":
                                        if (player.hasPermission("lineation.reload")) {
                                            LineIO.saveAll();
                                            Config.getPluginConfig().saveConfig();
                                            Config.getData().saveConfig();
                                            Config.getLineConfig().reloadConfig();
                                            Config.getPluginConfig().reloadConfig();
                                            Config.getData().reloadConfig();
                                            Config.getMessageConfig().reloadConfig();
                                            LineIO.loadAll();
                                            player.sendMessage(Message.SUCCESS_RELOAD);
                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                        break;
                                    case "maxwins":
                                        if (player.hasPermission("lineation.maxwins")) {
                                            try {
                                                int i = Integer.parseInt(args[2]);
                                                Config.getPluginConfig().getConfig().set("maxwins", i);
                                                player.sendMessage(Message.SUCCESS_OPTION_SET);
                                            } catch (Exception e) {
                                                player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                            }
                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                        break;
                                    case "forget":
                                        if (player.hasPermission("lineation.forget")) {
                                            try {
                                                if (Config.getData().getConfig().isInt(args[2])) {
                                                    Config.getData().getConfig().set(args[2], null);
                                                    player.sendMessage(Message.SUCCESS_PLAYER_FORGOTTEN.replace("$PLAYER$", args[2]));
                                                } else if (args[2].equals("all")) {
                                                    Lineation.getInstance().saveResource("data.yml", true);
                                                    Config.getData().reloadConfig();
                                                    player.sendMessage(Message.SUCCESS_PLAYER_FORGOTTEN.replace("$PLAYER$", args[2]));
                                                } else if (Config.getData().getConfig().isInt(Bukkit.getPlayer(args[2]).getUniqueId().toString())) {
                                                    Config.getData().getConfig().set(Bukkit.getPlayer(args[2]).getUniqueId().toString(), null);
                                                    player.sendMessage(Message.SUCCESS_PLAYER_FORGOTTEN.replace("$PLAYER$", args[2]));
                                            } else player.sendMessage(Message.ERROR_UNKNOWN_PLAYER.replace("$PLAYER$", args[2]));
                                            } catch (Exception e) {
                                                player.sendMessage(Message.ERROR_UNKNOWN_PLAYER.replace("$PLAYER$", args[2]));
                                            }
                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                        break;
                                    default:
                                        player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                }
                                break;
                            } player.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help"));
                            break;
                        case "line": 
                            if (args.length > 1) {
                                switch (args[1].toLowerCase()) {
                                    case "create":
                                        if (args.length > 2) {
                                            switch (args[2].toLowerCase()) {
                                                case "start":
                                                case "finish":
                                                    if (player.hasPermission("lineation.line.create")) {
                                                        if (args.length > 3) {
                                                            if (Line.getLines().get(args[3]) == null) {
                                                                if (!args[3].equalsIgnoreCase("help")
                                                                    && !args[3].equalsIgnoreCase("create")
                                                                    && !args[3].equalsIgnoreCase("remove")
                                                                    && !args[3].equalsIgnoreCase("list")) {
                                                                    new Line(args[3], args[2]);
                                                                    player.sendMessage(Message.SUCCESS_LINE_CREATED.replace("$LINE$", args[3]));
                                                                } else player.sendMessage(Message.ERROR_INVALID_NAME);
                                                            } else player.sendMessage(Message.ERROR_LINE_EXISTS);
                                                        } else player.sendMessage(Message.ERROR_MISSING_ARGS.replace("$MISSING$", "<name>"));
                                                        break;
                                                    } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                    break;
                                                default:
                                                    player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                    break;
                                            } break;
                                        }
                                        else player.sendMessage(Message.ERROR_MISSING_ARGS.replace("$MISSING$", "<type>, <name>"));
                                        break;
                                    case "list":
                                        if (args.length > 2) {
                                            switch (args[2].toLowerCase()) {
                                                case "start":
                                                    if (player.hasPermission("lineation.line.list")) {
                                                        player.sendMessage(Message.LINE_LIST.replace("$LINES$",
                                                                    Line.getStartLines().keySet().toString()
                                                                        .replace("[", "").replace("]", "")));
                                                    } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                    break;
                                                case "finish":
                                                    if (player.hasPermission("lineation.line.list")) {
                                                        player.sendMessage(Message.LINE_LIST.replace("$LINES$",
                                                                    Line.getFinishLines().keySet().toString()
                                                                        .replace("[", "").replace("]", "")));
                                                    } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                    break;
                                                case "started":
                                                    if (player.hasPermission("lineation.line.list")) {
                                                        player.sendMessage(Message.LINE_LIST.replace("$LINES$",
                                                                    Line.getStartedLines().keySet().toString()
                                                                        .replace("[", "").replace("]", "")));
                                                    } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                    break;
                                                default:
                                                    player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                    break;
                                            }
                                        } else if (player.hasPermission("lineation.line.list")) {
                                                        player.sendMessage(Message.LINE_LIST.replace("$LINES$",
                                                                    Line.getLines().keySet().toString()
                                                                        .replace("[", "").replace("]", "")));
                                            } else player.sendMessage(Message.ERROR_NO_PERMS);
                                        break;
                                    case "remove":
                                        if (player.hasPermission("lineation.line.remove")) {
                                            if (args.length > 2 && Line.getLines().get(args[2]) != null) {
                                                if (Line.getLines().get(args[2]).getLinkedLine() != "") {
                                                    Line.getLines().remove(Line.getLines().get(args[2]).getLinkedLine());
                                                }
                                                Line.getLines().remove(args[2]);
                                                player.sendMessage(Message.SUCCESS_LINE_REMOVED
                                                        .replace("$LINE$", args[2]));
                                            } else player.sendMessage(Message.ERROR_UNKNOWN_LINE.replace("$LINE$", args[2]));
                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                        break;
                                    default:
                                        if (Line.getLines().get(args[1]) != null) {
                                            if (args.length > 2) {
                                                Line line = Line.getLines().get(args[1]);
                                                switch (args[2].toLowerCase()) {
                                                    case "info":
                                                        if (player.hasPermission("lineation.line.info")) {
                                                            if (args.length > 3) {
                                                                if (args[3].equalsIgnoreCase("options")) {
                                                                    LineTools.getLineOptions(line, player);
                                                                    break;
                                                                } else player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                            } LineTools.getLineInfo(line, player);
                                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                        break;
                                                    case "getwinners":
                                                        if (player.hasPermission("lineation.line.getwinners")) {
                                                            if (line.getType().equalsIgnoreCase("finish")) {
                                                                LineTools.getWinnersString(line, player);
                                                            } else player.sendMessage(Message.ERROR_NOT_FINISH.replace("$LINE$", args[2]));
                                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                        break;
                                                    case "setarea":
                                                        if (player.hasPermission("lineation.line.setarea")) {
                                                            line.setArea(player);
                                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                        break;
                                                    case "addborder":
                                                        if (player.hasPermission("lineation.line.addborder")) {
                                                            line.addBorder(player);
                                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                        break;
                                                    case "removeborder":
                                                        if (player.hasPermission("lineation.line.removeborder")) {
                                                            if (args.length > 3) {
                                                                switch (args[3].toLowerCase()) {
                                                                    case "all":
                                                                        line.clearBorders();
                                                                        player.sendMessage(Message.SUCCESS_BORDER_REMOVED);
                                                                        break;
                                                                    default:
                                                                        try { 
                                                                            int i = Integer.parseInt(args[3]);
                                                                            line.removeBorder(i);
                                                                        } catch (Exception e) {
                                                                            player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                                        }
                                                                        break;
                                                                }
                                                                break;
                                                            } else player.sendMessage(Message.ERROR_MISSING_ARGS.replace("$MISSING$", "<all/nunber>"));
                                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                        break;
                                                    case "start":
                                                        if (player.hasPermission("lineation.line.start")) {
                                                            LineTools.startLine(line);
                                                            player.sendMessage(Message.SUCCESS_LINE_STARTED
                                                                    .replace("$LINE$", args[1]));
                                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                        break;
                                                    case "stop":
                                                        if (player.hasPermission("lineation.line.stop")) {
                                                            LineTools.stopLine(line);
                                                            player.sendMessage(Message.SUCCESS_LINE_STOPPED
                                                                    .replace("$LINE$", args[1]));
                                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                        break;
                                                    case "option":
                                                        if (args.length > 3) {
                                                            switch (args[3].toLowerCase()) {
                                                                case "maxwinners":
                                                                    if (args.length > 4) {
                                                                        if (player.hasPermission("lineation.line.option.maxwinners")) {
                                                                            try {
                                                                                int i = Integer.parseInt(args[4]);
                                                                                line.setMaxWinners(i);
                                                                                player.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                            } catch (Exception e) {
                                                                                player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                                            }
                                                                            break;
                                                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                                    } player.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                                    break;
                                                                case "messagereach":
                                                                    if (args.length > 4) {
                                                                        switch (args[4].toLowerCase()) {
                                                                            case "world":
                                                                            case "disabled":
                                                                            case "all":
                                                                                if (player.hasPermission("lineation.line.option.messagereach")) {
                                                                                    line.setMessageReach(args[4]);
                                                                                    player.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                                } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                                                break;
                                                                            default:
                                                                                player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                                        }
                                                                        break;
                                                                    } player.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                                    break;
                                                                case "gamemodes":
                                                                    if (args.length > 4) {
                                                                        if (player.hasPermission("lineation.line.option.gamemodes")) {
                                                                            try {
                                                                                line.setGameModes(args[4].toUpperCase());
                                                                                player.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                            } catch (Exception e) {
                                                                                player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                                            }
                                                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                                        break;
                                                                    } player.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                                    break;
                                                                case "blocksequence":
                                                                    if (args.length > 4) {
                                                                        if (player.hasPermission("lineation.line.option.blocksequence")) {
                                                                            try {
                                                                                line.setBlockSequence(args[4].toLowerCase());
                                                                                player.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                            } catch (Exception e) {
                                                                                player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                                            }
                                                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                                        break;
                                                                    } player.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                                    break;
                                                                case "teleport":
                                                                    if (args.length > 4) {
                                                                        if (player.hasPermission("lineation.line.option.teleport")) {
                                                                            switch (args[4].toLowerCase()) {
                                                                                case "here":
                                                                                    line.setTeleportLocation(player);
                                                                                    line.setTeleportEnabled(true);
                                                                                    player.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                                    break;
                                                                                case "disable":
                                                                                    line.setTeleportEnabled(false);
                                                                                    player.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                                    break;
                                                                                default:
                                                                                player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                                            }
                                                                            break;
                                                                        } player.sendMessage(Message.ERROR_NO_PERMS);
                                                                        break;
                                                                    } player.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                                    break;
                                                                case "link":
                                                                    if (args.length > 4) {
                                                                        if (player.hasPermission("lineation.line.option.link")) {
                                                                            if (Line.getLines().get(args[4]) != null) {
                                                                                Line linkLine = Line.getLines().get(args[4]);
                                                                                if (line.getType().equalsIgnoreCase("start")) {
                                                                                    if (linkLine.getType().equalsIgnoreCase("finish")) {
                                                                                        line.setLinkedLine(args[4]);
                                                                                        linkLine.setLinkedLine(args[1]);
                                                                                        player.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                                        break;
                                                                                    } player.sendMessage(Message.ERROR_NOT_FINISH.replace("$LINE$", args[4]));
                                                                                } else if (line.getType().equalsIgnoreCase("finish")) {
                                                                                    if (linkLine.getType().equalsIgnoreCase("start")) {
                                                                                        line.setLinkedLine(args[4]);
                                                                                        linkLine.setLinkedLine(args[1]);
                                                                                        player.sendMessage(Message.SUCCESS_OPTION_SET);
                                                                                        break;
                                                                                    } player.sendMessage(Message.ERROR_NOT_START.replace("$LINE$", args[4]));
                                                                                }
                                                                            } else player.sendMessage(Message.ERROR_UNKNOWN_LINE.replace("$LINE$", args[4]));
                                                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                                    } else player.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                                    break;
                                                                default:
                                                                    player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                            }
                                                            break;
                                                        } else player.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help options"));
                                                        break;
                                                    default:
                                                        player.sendMessage(Message.ERROR_SEE_HELP.replace("$COMMAND$", "/lineation help lines"));
                                                }
                                            } else player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                                
                                        } else player.sendMessage(Message.ERROR_UNKNOWN_LINE.replace("$LINE$", args[2]));            
                                        break;
                                }
                            } else if (player.hasPermission("lineation.help")) {
                                        player.sendMessage(Message.HELP_LINE);
                                     } else player.sendMessage(Message.ERROR_NO_PERMS);
                        break;
                        default:
                            player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                            break;
                        }
                    } else if (player.hasPermission("lineation.help")) player.sendMessage(Message.HELP_INDEX);
                        else player.sendMessage(Message.ERROR_NO_PERMS);
                }
            } else sender.sendMessage("Please run this command as a player.");
        return true;
    }
}
