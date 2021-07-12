package com.diademiemi.lineation;

import org.bukkit.ChatColor;

/**
 * Messages used within the plugin
 *
 * @author diademiemi
 */

public class Message {

    private static final Config messageConfig = Config.getMessageConfig();

    public static String PREFIX;
    public static String HELP_INDEX;
    public static String ERROR_NO_PERMS;
    public static String ERROR_UNKNOWN_ARGS;
    public static String SUCCESS_RELOAD;

    /**
     * Load/reload messages from file
     */
    public static void reloadMessages() {
        PREFIX = format(messageConfig.getConfig().getString("PREFIX"));
        HELP_INDEX = format(messageConfig.getConfig().getString("HELP_INDEX"));
        ERROR_NO_PERMS = PREFIX + format(messageConfig.getConfig().getString("ERROR_NO_PERMS"));
        ERROR_UNKNOWN_ARGS = PREFIX + format(messageConfig.getConfig().getString("ERROR_UNKNOWN_ARGS"));
        SUCCESS_RELOAD = PREFIX + format(messageConfig.getConfig().getString("SUCCESS_RELOAD"));
    }

    /**
     * Apply colour codes and line breaks
     *
     * @param msg mesage to format
     * @return formatted message
     */
    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}

