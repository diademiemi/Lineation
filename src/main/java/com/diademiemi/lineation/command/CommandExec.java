package com.diademiemi.lineation.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.diademiemi.lineation.Message;
import com.diademiemi.lineation.Config;
import com.diademiemi.lineation.line.Line;

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
                            if (player.hasPermission("lineation.help")) player.sendMessage(Message.HELP_INDEX);
                            else player.sendMessage(Message.ERROR_NO_PERMS);
                            break;
                        case "reload":
                            if (player.hasPermission("lineation.reload")) {
                                Config.getLineConfig().reloadConfig();
                                Config.getDefaultsConfig().reloadConfig();
                                Config.getMessageConfig().reloadConfig();
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
                                                    if (player.hasPermission("lineation.line.help")) {
                                                        player.sendMessage(Message.HELP_LINE);
                                                    } else player.sendMessage(Message.ERROR_NO_PERMS);
                                                    break;
                                                default:
                                                    player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                            }
                                        }
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
                                                default:
                                                    player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                            }
                                        } else if (player.hasPermission("lineation.line.list")) {
                                                        player.sendMessage(Message.LINE_LIST.replace("$LINES$",
                                                                    Line.getLines().keySet().toString()
                                                                        .replace("[", "").replace("]", "")));
                                            } else player.sendMessage(Message.ERROR_NO_PERMS);
                                        break;
                                    case "help":
                                        if (player.hasPermission("lineation.line.help")) {
                                            player.sendMessage(Message.HELP_LINE);
                                        } else player.sendMessage(Message.ERROR_NO_PERMS);
                                        break;
                                    default:
                                        player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                                }
                            } else if (player.hasPermission("lineation.line.help")) {
                                        player.sendMessage(Message.HELP_LINE);
                                     } else player.sendMessage(Message.ERROR_NO_PERMS);
                        break;
                        default:
                            player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                        }
                    } else if (player.hasPermission("lineation.help")) player.sendMessage(Message.HELP_INDEX);
                        else player.sendMessage(Message.ERROR_NO_PERMS);
                }
            } else sender.sendMessage("Please run this command as a player.");
        return true;
    }
}
