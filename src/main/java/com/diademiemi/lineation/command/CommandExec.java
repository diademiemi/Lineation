package com.diademiemi.lineation.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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

            if (label.equalsIgnoreCase("lineation") || label.equalsIgnoreCase("la")) {
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
                        case "reload":
                            if (player.hasPermission("lineation.reload")) {
                                LineIO.saveAll();
                                Config.getLineConfig().reloadConfig();
                                Config.getDefaultsConfig().reloadConfig();
                                Config.getMessageConfig().reloadConfig();
                                LineIO.loadAll();
                                player.sendMessage(Message.SUCCESS_RELOAD);
                            } else player.sendMessage(Message.ERROR_NO_PERMS);
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
                                                            LineTools.getLineInfo(line, player);
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
                                                                case "maxwins":
                                                                    if (args.length > 4) {
                                                                        if (player.hasPermission("lineation.line.option.maxwins")) {
                                                                            try {
                                                                                int i = Integer.parseInt(args[4]);
                                                                                line.setMaxWinners(i);
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
