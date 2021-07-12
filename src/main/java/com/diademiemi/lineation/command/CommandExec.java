package com.diademiemi.lineation.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.diademiemi.lineation.Message;
import com.diademiemi.lineation.Config;

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
                            if (player.hasPermission("volleyball.reload")) {
                                Config.getLineConfig().reloadConfig();
                                Config.getDefaultsConfig().reloadConfig();
                                Config.getMessageConfig().reloadConfig();
                                player.sendMessage(Message.SUCCESS_RELOAD);
                            } else player.sendMessage(Message.ERROR_NO_PERMS);
                            break;
                        default:
                            player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
                    }
                } else player.sendMessage(Message.ERROR_UNKNOWN_ARGS);
            }
        } else sender.sendMessage("Please run this command as a player.");
        return true;
    }
}
