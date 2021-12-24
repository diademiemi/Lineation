package me.diademiemi.lineation;

import org.bukkit.ChatColor;

/**
 * Messages used within the plugin
 *
 * @author diademiemi
 */

public class Message {

    private static final Config messageConfig = Config.getMessageConfig();

    public static String PREFIX;
	public static String HELP_GENERAL;
	public static String HELP_LINES;
	public static String HELP_START;
	public static String HELP_FINISH;
    public static String HELP_OPTIONS;
	public static String HELP_OPTIONS_START;
	public static String HELP_OPTIONS_FINISH;
	public static String HELP_AREAS;
	public static String HELP_BORDERS;
	public static String GUIDE_CREATED_START;
	public static String GUIDE_CREATED_FINISH;
	public static String GUIDE_AREA_SET_START;
	public static String GUIDE_AREA_SET_FINISH;
	public static String GUIDE_BORDER_SET_START;
	public static String GUIDE_BORDER_SET_FINISH;
	public static String GUIDE_SUGGEST_LINK_START;
	public static String GUIDE_SUGGEST_LINK_FINISH;
    public static String ERROR_NO_PERMS;
    public static String ERROR_UNKNOWN_ARGS;
    public static String ERROR_MISSING_ARGS;
    public static String ERROR_UNKNOWN_PLAYER;
    public static String ERROR_SEE_HELP;
    public static String ERROR_UNKNOWN_LINE;
    public static String ERROR_NULL_AREA;
    public static String ERROR_INVALID_NAME;
	public static String ERROR_INVALID_BLOCK;
	public static String ERROR_INVALID_GAMEMODE;
    public static String ERROR_ALREADY_STARTED;
    public static String ERROR_NOT_START;
    public static String ERROR_NOT_FINISH;
    public static String ERROR_NO_CHECKPOINT;
    public static String ERROR_NO_ILLEGAL_AREA;
    public static String ERROR_NO_LINE_HERE;
    public static String ERROR_LINE_EXISTS;
    public static String SUCCESS_RELOAD;
    public static String SUCCESS_LINE_STARTED;
    public static String SUCCESS_LINE_STOPPED;
    public static String SUCCESS_LINE_CREATED;
    public static String SUCCESS_SET_AREA;
    public static String SUCCESS_SET_BORDER;
    public static String SUCCESS_SET_CHECKPOINT;
    public static String SUCCESS_SET_ILLEGAL_AREA;
    public static String SUCCESS_LINE_REMOVED;
    public static String SUCCESS_BORDER_REMOVED;
    public static String SUCCESS_CHECKPOINT_REMOVED;
	public static String SUCCESS_ILLEGAL_AREA_REMOVED;
    public static String SUCCESS_OPTION_SET;
    public static String SUCCESS_PLAYER_FORGOTTEN;
    public static String ALREADY_STOPPED;
    public static String LINE_INFO_START;
    public static String LINE_INFO_FINISH;
    public static String LINE_OPTIONS_START;
    public static String LINE_OPTIONS_FINISH;
    public static String LINE_LIST;
    public static String LINE_WINNERS;
    public static String STARTING_IN;
    public static String STARTING_NOW;
    public static String PLAYER_FINISHED;
    public static String PLAYER_FINISHED_NOT_COUNTED;
    public static String PLAYER_LAP;
    public static String FINISH_CLOSE;
    public static String FINISH_CLOSE_PLAYER;

    /**
     * Load/reload messages from file
     */
    public static void reloadMessages() {
        PREFIX = format(messageConfig.getConfig().getString("PREFIX"));
        HELP_GENERAL = format(messageConfig.getConfig().getString("HELP_GENERAL"));
        HELP_LINES = format(messageConfig.getConfig().getString("HELP_LINES"));
        HELP_START = format(messageConfig.getConfig().getString("HELP_START"));
        HELP_FINISH = format(messageConfig.getConfig().getString("HELP_FINISH"));
        HELP_OPTIONS = format(messageConfig.getConfig().getString("HELP_OPTIONS"));
        HELP_OPTIONS_START = format(messageConfig.getConfig().getString("HELP_OPTIONS_START"));
        HELP_OPTIONS_FINISH = format(messageConfig.getConfig().getString("HELP_OPTIONS_FINISH"));
        HELP_AREAS = format(messageConfig.getConfig().getString("HELP_AREAS"));
        HELP_BORDERS = format(messageConfig.getConfig().getString("HELP_BORDERS"));
		GUIDE_CREATED_START = format(messageConfig.getConfig().getString("GUIDE_CREATED_START"));
		GUIDE_CREATED_FINISH = format(messageConfig.getConfig().getString("GUIDE_CREATED_FINISH"));
		GUIDE_AREA_SET_START = format(messageConfig.getConfig().getString("GUIDE_AREA_SET_START"));
		GUIDE_AREA_SET_FINISH = format(messageConfig.getConfig().getString("GUIDE_AREA_SET_FINISH"));
		GUIDE_BORDER_SET_START = format(messageConfig.getConfig().getString("GUIDE_BORDER_SET_START"));
		GUIDE_BORDER_SET_FINISH = format(messageConfig.getConfig().getString("GUIDE_BORDER_SET_FINISH"));
		GUIDE_SUGGEST_LINK_START = format(messageConfig.getConfig().getString("GUIDE_SUGGEST_LINK_START"));
		GUIDE_SUGGEST_LINK_FINISH = format(messageConfig.getConfig().getString("GUIDE_SUGGEST_LINK_FINISH"));
        ERROR_NO_PERMS = PREFIX + format(messageConfig.getConfig().getString("ERROR_NO_PERMS"));
        ERROR_UNKNOWN_ARGS = PREFIX + format(messageConfig.getConfig().getString("ERROR_UNKNOWN_ARGS"));
        ERROR_MISSING_ARGS = PREFIX + format(messageConfig.getConfig().getString("ERROR_MISSING_ARGS"));
        ERROR_UNKNOWN_PLAYER = PREFIX + format(messageConfig.getConfig().getString("ERROR_UNKNOWN_PLAYER"));
        ERROR_SEE_HELP = PREFIX + format(messageConfig.getConfig().getString("ERROR_SEE_HELP"));
        ERROR_UNKNOWN_LINE = PREFIX + format(messageConfig.getConfig().getString("ERROR_UNKNOWN_LINE"));
        ERROR_NULL_AREA = PREFIX + format(messageConfig.getConfig().getString("ERROR_NULL_AREA"));
        ERROR_INVALID_NAME = PREFIX + format(messageConfig.getConfig().getString("ERROR_INVALID_NAME"));
        ERROR_INVALID_BLOCK = PREFIX + format(messageConfig.getConfig().getString("ERROR_INVALID_BLOCK"));
        ERROR_INVALID_GAMEMODE = PREFIX + format(messageConfig.getConfig().getString("ERROR_INVALID_GAMEMODE"));
        ERROR_ALREADY_STARTED = PREFIX + format(messageConfig.getConfig().getString("ERROR_ALREADY_STARTED"));
        ERROR_NOT_START = PREFIX + format(messageConfig.getConfig().getString("ERROR_NOT_START"));
        ERROR_NOT_FINISH = PREFIX + format(messageConfig.getConfig().getString("ERROR_NOT_FINISH"));
        ERROR_NO_CHECKPOINT = PREFIX + format(messageConfig.getConfig().getString("ERROR_NO_CHECKPOINT"));
        ERROR_NO_ILLEGAL_AREA = PREFIX + format(messageConfig.getConfig().getString("ERROR_NO_ILLEGAL_AREA"));
        ERROR_NO_LINE_HERE = PREFIX + format(messageConfig.getConfig().getString("ERROR_NO_LINE_HERE"));
        ERROR_LINE_EXISTS = PREFIX + format(messageConfig.getConfig().getString("ERROR_LINE_EXISTS"));
        SUCCESS_RELOAD = PREFIX + format(messageConfig.getConfig().getString("SUCCESS_RELOAD"));
        SUCCESS_LINE_STARTED = PREFIX + format(messageConfig.getConfig().getString("SUCCESS_LINE_STARTED"));
        SUCCESS_LINE_STOPPED = PREFIX + format(messageConfig.getConfig().getString("SUCCESS_LINE_STOPPED"));
        SUCCESS_LINE_CREATED = PREFIX + format(messageConfig.getConfig().getString("SUCCESS_LINE_CREATED"));
        SUCCESS_SET_AREA = PREFIX + format(messageConfig.getConfig().getString("SUCCESS_SET_AREA"));
        SUCCESS_SET_BORDER = PREFIX + format(messageConfig.getConfig().getString("SUCCESS_SET_BORDER"));
        SUCCESS_SET_CHECKPOINT = PREFIX + format(messageConfig.getConfig().getString("SUCCESS_SET_CHECKPOINT"));
        SUCCESS_SET_ILLEGAL_AREA = PREFIX + format(messageConfig.getConfig().getString("SUCCESS_SET_ILLEGAL_AREA"));
        SUCCESS_LINE_REMOVED = PREFIX + format(messageConfig.getConfig().getString("SUCCESS_LINE_REMOVED"));
        SUCCESS_BORDER_REMOVED = PREFIX + format(messageConfig.getConfig().getString("SUCCESS_BORDER_REMOVED"));
        SUCCESS_CHECKPOINT_REMOVED = PREFIX + format(messageConfig.getConfig().getString("SUCCESS_CHECKPOINT_REMOVED"));
        SUCCESS_ILLEGAL_AREA_REMOVED = PREFIX + format(messageConfig.getConfig().getString("SUCCESS_ILLEGAL_AREA_REMOVED"));
        SUCCESS_OPTION_SET = PREFIX + format(messageConfig.getConfig().getString("SUCCESS_OPTION_SET"));
        SUCCESS_PLAYER_FORGOTTEN = PREFIX + format(messageConfig.getConfig().getString("SUCCESS_PLAYER_FORGOTTEN"));
        ALREADY_STOPPED = PREFIX + format(messageConfig.getConfig().getString("ALREADY_STOPPED"));
        LINE_INFO_START = PREFIX + format(messageConfig.getConfig().getString("LINE_INFO_START"));
        LINE_INFO_FINISH = PREFIX + format(messageConfig.getConfig().getString("LINE_INFO_FINISH"));
        LINE_OPTIONS_START = PREFIX + format(messageConfig.getConfig().getString("LINE_OPTIONS_START"));
        LINE_OPTIONS_FINISH = PREFIX + format(messageConfig.getConfig().getString("LINE_OPTIONS_FINISH"));
        LINE_LIST = PREFIX + format(messageConfig.getConfig().getString("LINE_LIST"));
        LINE_WINNERS = PREFIX + format(messageConfig.getConfig().getString("LINE_WINNERS"));
        STARTING_IN = format(messageConfig.getConfig().getString("STARTING_IN"));
        STARTING_NOW = format(messageConfig.getConfig().getString("STARTING_NOW"));
        PLAYER_FINISHED = format(messageConfig.getConfig().getString("PLAYER_FINISHED"));
        PLAYER_FINISHED_NOT_COUNTED = format(messageConfig.getConfig().getString("PLAYER_FINISHED_NOT_COUNTED"));
        PLAYER_LAP = format(messageConfig.getConfig().getString("PLAYER_LAP"));
        FINISH_CLOSE = format(messageConfig.getConfig().getString("FINISH_CLOSE"));
        FINISH_CLOSE_PLAYER = format(messageConfig.getConfig().getString("FINISH_CLOSE_PLAYER"));
    }

    /**
     * Apply colour codes and line breaks
     *
     * @param msg   Message to format
     * @return  Formatted message with colours
     */
    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * get ordinal indicator from number
     *
     * @param i Number to convert
     * @return  String of ordinal indicator
     */
    public static String ordinal(int i) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
        case 11:
        case 12:
        case 13:
            return i + "th";
        default:
            return i + suffixes[i % 10];

        }
    }
}

