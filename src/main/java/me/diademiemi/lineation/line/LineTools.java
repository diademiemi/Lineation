package me.diademiemi.lineation.line;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.MaxChangedBlocksException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.World;

import me.diademiemi.lineation.Lineation;
import me.diademiemi.lineation.Message;
import me.diademiemi.lineation.Config;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * Tools for lines
 *
 * @author diademiemi
 */
public class LineTools {

    private static final Config data = Config.getData();

    /**
     * Handle player finishing
     *
     * @param line  Line player finished in
     * @param player Player that finished
     */
    public static void playerFinish(Line line, Player player) {
        if (!line.isWinner(player) &&
                line.getPlayerCheckpoint(player) == line.getCheckpoints().size()) {
            line.addPlayerCheckpoint(player, 0);
            line.addPlayerLap(player, line.getPlayerLaps(player) + 1);

            if (line.getLaps() == line.getPlayerLaps(player)) {

                String uuid = player.getUniqueId().toString();
                if (!data.getConfig().isInt(uuid) ||
                    data.getConfig().getInt(uuid) != Config.getPluginConfig().getConfig().getInt("maxwins")) {

                    line.addWinner(player);
                    finishMessage(line, player, line.getWinners().size());

                    if (line.isTeleportEnabled()) {
                        player.teleport(line.getTeleportLocation());
                    }

                    ArrayList<String> commands = line.getCommands();

                    for (String c : commands) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), c
                                .replace("%player%", player.getName())
                                .replace("%uuid%", uuid));
                    }

                    data.getConfig().set(uuid, data.getConfig().getInt(uuid) + 1);

                    if (line.getWinners().size() == line.getMaxWinners()) {
                        LineTools.stopLine(line);
                    }

                }
            } else LineTools.lapMessage(line, player, line.getPlayerLaps(player));

        }
    }

	/**
	 * Get a list of players that a message should be sent to according to the lines messagereach option
	 *
	 * @param line	Line to get message reach from
	 *
	 * @return	List of players
	 */
	public static List<Player> getMessagePlayers(Line line) {
		List<Player> players;
		switch (line.getMessageReach()) {
			case "all":
				players = new ArrayList<>(Bukkit.getOnlinePlayers());
				break;
            case "world":
                players = line.getWorld().getPlayers();
				break;
			case "area":
				players = line.getPlayers();
				break;
			case "disabled":
				players = new ArrayList<>();
				break;
			default:
				try {
					int i = Integer.parseInt(line.getMessageReach());
					double[][] a = line.getArea();
					double[] areaCenter = new double[3];
					areaCenter[0] = (a[0][0] + a[1][0]) / 2.0;
					areaCenter[1] = (a[0][1] + a[1][1]) / 2.0;
					areaCenter[2] = (a[0][2] + a[1][2]) / 2.0;
					Location centerLocation = new Location(line.getWorld(), areaCenter[0], areaCenter[1], areaCenter[2]);
					players = new ArrayList<>();
					for (Player p : line.getWorld().getPlayers()) {
						if (centerLocation.distance(p.getLocation()) < (double)i) {
							players.add(p);
						}
					}
				} catch (Exception e) {
					players = new ArrayList<>();
				}
				break;
		}
		return players;
	}
	
  
    /**
     * Send finish message when someone finishes
     *
     * @param line  Line player finished in
     * @param player    PLayer that finished
     * @param place Place number player got
     */
    public static void finishMessage(Line line, Player player, Integer place) {
		for (Player p : getMessagePlayers(line)) {
			p.sendMessage(Message.PLAYER_FINISHED
					.replace("$NAME$", player.getName())
					.replace("$PLACE$", Message.ordinal(place)));
		}
    }
   
    /**
     * Send announcement message when someone completes a lap
     *
     * @param line  Line player lapped
     * @param player    Player that finished
     * @param i Number of the lap
     */
    public static void lapMessage(Line line, Player player, Integer i) {
		for (Player p : getMessagePlayers(line)) {
			p.sendMessage(Message.PLAYER_LAP
					.replace("$NAME$", player.getName())
					.replace("$LAP$", i + "/" + line.getLaps()));
		}
    }

    /**
     * Announce winners when finish line closes
     *
     * @param line  Line that has closed
     */
    public static void finishCloseMessage(Line line) {
        StringBuilder announcement = new StringBuilder("");
        @SuppressWarnings("unchecked")
        ArrayList<String> winners = line.getWinners();
        int i = 1;

        announcement.append(Message.FINISH_CLOSE);
        for (String p : winners) {
            announcement.append("\n");
            announcement.append(Message.FINISH_CLOSE_PLAYER
                    .replace("$NAME$", p)
                    .replace("$PLACE$", Message.ordinal(i)));
            i++;
        }

		for (Player p : getMessagePlayers(line)) {
			p.sendMessage(announcement.toString());
		}
    }
                    

    /**
     * Get line info as formatted message
     *
     * @param line  Line to get info of
     * @param player    Player to send message to
     */
    public static void getLineInfo(Line line, CommandSender player) {
        double[][] area = line.getArea();
        StringBuilder bordersString = new StringBuilder("");
        ArrayList<double[][]> borders = line.getBorders();
        ArrayList<double[][]> checkpoints = line.getCheckpoints();
        int i = 1;
        for (double[][] b : borders) {
            if (i> 1) {
                bordersString.append("\n");
            }
            bordersString.append(i);
            bordersString.append(": " + "(" + b[0][0] + "," + b[0][1] + "," + b[0][2] + ") -> (" +
                    b[1][0] + "," + b[1][1] + "," + b[1][2] + ")");
            i++;
        }
        switch (line.getType()) {
            case "start":
                player.sendMessage(Message.LINE_INFO_START
                        .replace("$NAME$", line.getName())
                        .replace("$LINKED$", line.getLinkedLine())
                        .replace("$STARTED$", Boolean.toString(line.isStarted()))
                        .replace("$TYPE$", line.getType())
                        .replace("$WORLD$", line.getWorld().getName())
                        .replace("$AREA$",
                            "(" + area[0][0] + "," + area[0][1] + "," + area[0][2] + ") -> (" +
                            area[1][0] + "," + area[1][1] + "," + area[1][2] + ")")
                        .replace("$BORDERS$", bordersString)
                        .replace(".0", ""));
                break;
            case "finish":
                StringBuilder checkpointsString = new StringBuilder("");
                i = 1;
                for (double[][] c : checkpoints) {
                    if (i> 1) {
                        checkpointsString.append("\n");
                    }
                    checkpointsString.append(i);
                    checkpointsString.append(": " + "(" + c[0][0] + "," + c[0][1] + "," + c[0][2] + ") -> (" +
                            c[1][0] + "," + c[1][1] + "," + c[1][2] + ")");
                    i++;
                }
                player.sendMessage(Message.LINE_INFO_FINISH
                        .replace("$NAME$", line.getName())
                        .replace("$LINKED$", line.getLinkedLine())
                        .replace("$STARTED$", Boolean.toString(line.isStarted()))
                        .replace("$TYPE$", line.getType())
                        .replace("$WORLD$", line.getWorld().getName())
                        .replace("$AREA$",
                            "(" + area[0][0] + "," + area[0][1] + "," + area[0][2] + ") -> (" +
                            area[1][0] + "," + area[1][1] + "," + area[1][2] + ")")
                        .replace("$BORDERS$", bordersString)
                        .replace("$CHECKPOINTS$", checkpointsString)
                        .replace(".0", ""));
                break;
        }
    }

    /**
     * Get line options info as formatted message
     *
     * @param line  Line to get info of
     * @param player    Player to send message to
     */
    public static void getLineOptions(Line line, CommandSender player) {
        StringBuilder teleportLocation = new StringBuilder("");
		teleportLocation.append("(" + (int)line.getTeleportLocation().getX() + "," +
				(int)line.getTeleportLocation().getY() + "," + 
				(int)line.getTeleportLocation().getZ() + ")");
        ArrayList<double[][]> illegalAreas = line.getIllegalAreas();
		int i = 1;
        switch (line.getType()) {
            case "start":
                StringBuilder illegalAreasString = new StringBuilder("");
                i = 1;
                for (double[][] ia : illegalAreas) {
                    if (i> 1) {
                        illegalAreasString.append("\n");
                    }
                    illegalAreasString.append(i);
                    illegalAreasString.append(": " + "(" + ia[0][0] + "," + ia[0][1] + "," + ia[0][2] + ") -> (" +
                            ia[1][0] + "," + ia[1][1] + "," + ia[1][2] + ")");
                    i++;
                }
                player.sendMessage(Message.LINE_OPTIONS_START
                        .replace("$NAME$", line.getName())
                        .replace("$BLOCKSEQUENCE$", line.getBlockSequenceString())
						.replace("$TPONSTART$", Boolean.toString(line.isTeleportEnabled()))
						.replace("$TPILLEGALAREA$", Boolean.toString(line.isTeleportEnabledIllegalArea()))
                        .replace("$TELEPORTLOCATION$", teleportLocation)
                        .replace("$MESSAGEREACH$", line.getMessageReach())
						.replace("$GAMEMODES$", line.getGameModesString())
						.replace("$ILLEGALAREAS$", illegalAreasString));
                break;
            case "finish":
                ArrayList<String> commands = line.getCommands();
                StringBuilder commandsString = new StringBuilder("");
                i = 1;
                for (String c : commands) {
                    if (i> 1) {
                        commandsString.append("\n");
                    }
                    commandsString.append(i);
                    commandsString.append(": " + c);
                    i++;
                }

                player.sendMessage(Message.LINE_OPTIONS_FINISH
                        .replace("$NAME$", line.getName())
                        .replace("$BLOCKSEQUENCE$", line.getBlockSequenceString())
						.replace("$TPONFINISH$", Boolean.toString(line.isTeleportEnabled()))
                        .replace("$TELEPORTLOCATION$", teleportLocation)
                        .replace("$ALLOWEDWINNERS$", Integer.toString(line.getMaxWinners()))
                        .replace("$LAPS$", Integer.toString(line.getLaps()))
                        .replace("$MESSAGEREACH$", line.getMessageReach())
                        .replace("$COMMANDS$", commandsString)
                        .replace("$GAMEMODES$", line.getGameModesString()));

                break;
        }           

    }

    /**
     * Get winners as formatted message
     * 
     * @param line  Line to get winners of
     * @param player    Player to send message to
     */
    public static void getWinnersString(Line line, CommandSender player) {
        StringBuilder winnersString = new StringBuilder("");
        @SuppressWarnings("unchecked")
        ArrayList<String> winners = line.getWinners();
        int i = 1;
        for (String p : winners) {
            if (i > 1) {
                winnersString.append(", ");
            }
            winnersString.append(Message.ordinal(i) + ": ");
            winnersString.append(p);
            i++;
        }
        player.sendMessage(Message.LINE_WINNERS
                .replace("$NAMES$", winnersString)); 
    }

    /**
     * Start start line
     *
     * @param line  Start line to start
     */
    public static void startStartLine(Line line) {
        line.setStarted();
        ArrayList<double[][]> borders = line.getBorders();
        ArrayList<String> blockSequence = line.getBlockSequence();
        ArrayList<Player> players = line.getPlayers();

        startLineSequence(borders, line, players, blockSequence, 1);

    }

    /**
     * Starts finish line
     *
     * @param line  Finish line to start
     */
    public static void startFinishLine(Line line) {
        line.clearWinners();
        line.setStarted();
        World world = line.getWorld();
        ArrayList<double[][]> borders = line.getBorders();
        
        String blockFrom = line.getBlockSequence().get(0);
        
        try (EditSession es = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {

            for (double[][] b : borders) {
                replaceBlocks(es, b, world, "air", blockFrom);
            }

        }

    }

    /**
     * Start a line
     *
     * @param line  Line to start
     */
    public static void startLine(Line line) {
        switch (line.getType()) {
            case "start":
                startStartLine(line);
                if (line.getLinkedLine() != "") {
                    startFinishLine(Line.getLines().get(line.getLinkedLine()));
                }
                break;
            case "finish":
                startFinishLine(line);
                if (line.getLinkedLine() != "") {
                    startStartLine(Line.getLines().get(line.getLinkedLine()));
                }
                break;
            }
    }

    /**
     * Stops start line
     *
     * @param line  Start line to stop
     */
    public static void stopStartLine(Line line) {
        line.setStopped();
        World world = line.getWorld();
        ArrayList<double[][]> borders = line.getBorders();
        String blockTo = line.getBlockSequence().get(0);

        try (EditSession es = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {

            for (double[][] b : borders) {
                replaceBlocks(es, b, world, blockTo, "air");
            }

        }

    }

    /** 
     * Stops a finish line
     *
     * @param line  Finish line to stop
     */
    public static void stopFinishLine(Line line) {
        line.setStopped();
        line.clearPlayerLaps();
        line.clearPlayerCheckpoints();
        World world = line.getWorld();
        ArrayList<double[][]> borders = line.getBorders();
        String blockTo = line.getBlockSequence().get(0);

        try (EditSession es = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {

            for (double[][] b : borders) {
                replaceBlocks(es, b, world, blockTo, "air");
            }

        }

        if (line.getWinners().size() != 0) {
            finishCloseMessage(line);
        }

    }

    /**
     * Stops a line
     *
     * @param line  Line to stop
     */
    public static void stopLine(Line line) {
        switch (line.getType()) {
            case "start":
                stopStartLine(line);
                if (line.getLinkedLine() != "") {
                    stopFinishLine(Line.getLines().get(line.getLinkedLine()));
                }
                break;
            case "finish":
                stopFinishLine(line);
                if (line.getLinkedLine() != "") {
                    stopStartLine(Line.getLines().get(line.getLinkedLine()));
                }
                break;
        }
    }

	/**
	 * Method to validate if block sequence is valid
	 *
	 * @param sequence	String to validate
	 *
	 * @return	Boolean of whether string is valid
	 */
	public static boolean validateBlocks(String sequence) {
		ArrayList<String> blockList = new ArrayList<String>(Arrays.asList(sequence.split("\\s*,\\s*")));
		
		for (String s : blockList) {
			try {
				Material.valueOf(s.toUpperCase());
			} catch (IllegalArgumentException ex) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Method to validate if gamemodes exist
	 *
	 * @param modes	String to validate
	 *
	 * @return	Boolean of whether string is valid
	 */
	public static boolean validateGamemodes(String modes) {
		ArrayList<String> modesList = new ArrayList<String>(Arrays.asList(modes.split("\\s*,\\s*")));

		for (String s : modesList) {
			try {
				GameMode.valueOf(s.toUpperCase());
			} catch (IllegalArgumentException ex) {
				return false;
			}
		}
		
		return true;
	}

    /**
     * Method to replace blocks in a given area with a given EditSession with names of blocks to set
     *
     * @param es    WorldEdit EditSession to use
     * @param b Border edge coords to use
     * @param world World to set this in
     * @param blockTo   What block to set
     * @param blockFrom What block to replace
     */
    public static void replaceBlocks(EditSession es, double[][] b, World world, String blockTo, String blockFrom) {
        BlockVector3 min = BlockVector3.at(b[0][0], b[0][1], b[0][2]);
        BlockVector3 max = BlockVector3.at(b[1][0], b[1][1], b[1][2]);

        BlockTypeMask mask = new BlockTypeMask(BukkitAdapter.adapt(world), BlockTypes.get(blockFrom));

        CuboidRegion region = new CuboidRegion(BukkitAdapter.adapt(world), min, max); 

        try {
            es.replaceBlocks(region, mask, BlockTypes.get(blockTo).getDefaultState().toBaseBlock());

        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Method to start the start line sequence with one second delay per block to set, this calls replaceBlocks per line
     * This method reschedules itself if counter i has not reached the end of the sequence.
     *
     * @param borders   List of borders to set
     * @param line  The line
     * @param players   Players to send announcement sequence to
     * @param blockSequence ArrayList of strings with block sequence
     * @param i Integer to count amount of times this has looped
     */
    public static void startLineSequence(ArrayList<double[][]> borders, Line line, ArrayList<Player> players, ArrayList<String> blockSequence, Integer i) {
        Bukkit.getServer().getScheduler().runTaskLater(Lineation.getInstance(), new Runnable(){
            public void run() {
                try (EditSession es = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(line.getWorld()))) {

                    if (blockSequence.size() == i) {

                        for (double[][] b : borders) {
                            replaceBlocks(es, b, line.getWorld(), "air", blockSequence.get(i - 1));
                        }

                        for (Player p : getMessagePlayers(line)) {
                            p.sendMessage(Message.STARTING_NOW);
                        }

                        if (line.isTeleportEnabled()) {
                            for (Player p : players) {
								if(line.getGameModes().contains(p.getGameMode())) {
                                    p.teleport(line.getTeleportLocation());
								}
                            }
                        }

                    } else {

                        for (double[][] b : borders) {
                            replaceBlocks(es, b, line.getWorld(), blockSequence.get(i), blockSequence.get(i - 1));
                        }

                        for (Player p : getMessagePlayers(line)) {
                            p.sendMessage(Message.STARTING_IN.replace("$SECONDS$", String.valueOf(blockSequence.size() - i)));
                        }

                    }

                    if (blockSequence.size() != i) {
                        startLineSequence(borders, line, players, blockSequence, i + 1);
                    }

                }

            }

        },20L);
   
    }

}
