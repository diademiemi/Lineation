---
layout: default
---

# Commands & Permissions

## Info

The command to use this plugin is `/lineation`. There is an alias set by default as `/ln`.  
Some commands require a WorldEdit selection, these can not be used from command blocks.  

### Commands

#### Help Pages

- `/lineation help` :
  
  Main help page

- `/lineation help lines` :
  
  View commands relating to lines

- `/lineation help start` : 
  
  View commands and options for start lines

- `/lineation help finish` :
  
  View commands and options for finish lines

- `/lineation help options start` :
  
  View options you can set on start lines

- `/lineation help options finish` :
  
  View options you can set on finish lines

#### Start Lines

- `/lineation line <name> start` :  
   Starts this line, opens borders if there are any  

-  `/lineation line <name> stop` :  
   Stops this line, closes borders if there are any  

-  `/lineation line <name> setarea` :  
   Sets the area where players will be detected. Check /lineation help areas for explanation  

-  `/lineation line <name> addborder` :  
   Adds a border where blocks will be set at your current WorldEdit selection  

-  `/lineation line <name> removeborder <number/all>` :  
   Remove a border by number  

-  `/lineation line <name> link <name>` :  
   Link this line with a different line with the opposite type. This makes them start/stop at the same time.  

-  `/lineation line <name> tp` :  
   Teleports you to this lines teleport point

#### Finish Lines

- `/lineation line <name> start` :  
   Starts this line, opens borders if there are any  

-  `/lineation line <name> stop` :  
   Stops this line, closes borders if there are any and announces results to chat  

-  `/lineation line <name> setarea` :  
   Sets the area where players will be detected. Check /lineation help areas for explanation  

-  `/lineation line <name> addborder` :  
   Adds a border where blocks will be set at your current WorldEdit selection  

-  `/lineation line <name> removeborder <number/all>` :  
   Remove a border by number  

-  `/lineation line <name> addcheckpoint` :  
   Adds a checkpoint, players have to cross through these in order before winning  

-  `/lineation line <name> removecheckpoint <number/all>` :  
   Remove a checkpoint by number  

-  `/lineation line <name> getwinners` :  
   Get the players that last won this line  

-  `/lineation line <name> link <name>` :  
   Link this line with a different line with the opposite type. This makes them start/stop at the same time.  

-  `/lineation line <name> teleport` :  
   Teleports you to this lines teleport point

### Start Line Options

- `/lineation line <name> option blocksequence <block1 block2 block3>` :  
   A list of blocks to set the borders to when the line is stopped or opening  

-  `/lineation line <name> option illegalarea add` :  
   Add an area that when entered by a player will teleport them back to the teleport point  

-  `/lineation line <name> option illegalarea remove <number/all>` :  
   Remove an illegal areas by number  

-  `/lineation line <name> option messagereach <all/area/world/number/disabled>` :  
   Sets if the messages will target all players, players in the area, in the world or a certain distance from the area.  

-  `/lineation line <name> option teleport setlocation` :  
   Sets the location which will be used to teleport players to if this feature is used  

-  `/lineation line <name> option teleport gamemodes` :  
   A list of gamemodes affected by the teleport feature  

-  `/lineation line <name> option teleport <onstart/illegalarea> <true/false>` :  
   Whether players get teleported when this line opens or when they enter an illegal area

### Finish Line Options

- `/lineation line <name> option block <block>` :  
   The block to set the borders to when the line is stopped  

-  `/lineation line <name> option gamemodes` :  
   A list of gamemodes that players need to be in to be counted to finish  

-  `/lineation line <name> option laps <number>` :   
   Amount of laps a player has to do for this course to win. Requires at least one checkpoint to be set!  

-  `/lineation line <name> option maxwinners <number>` :  
   Sets the amount of players that need to finish before the line closes  

-  `/lineation line <name> option messagereach <all/area/world/number/disabled>` :  
   Sets if the messages will target all players, players in the area, in the world or a certain distance from the area.  

-  `/lineation line <name> option teleport setlocation` :  
   Sets the location which will be used to teleport players to if this feature is used  

-  `/lineation line <name> option teleport onfinish <true/false>` :  
   Whether players get teleported when to the teleport point when they finish

### Permissions

- `lineation.help` - Allow using the help commands
- `lineation.config.reload` - Allow reloading the plugin config files
- `lineation.config.maxwins` - Allow setting the maximum amount of times someone can win
- `lineation.config.forget` - Allow forgetting a players wins
- `lineation.line.list` - Allow seeing the list of lines
- `lineation.line.here` - Allow seeing information about the line you're currently standing in
- `lineation.line.create` - Allow creating new lines
- `lineation.line.remove` - Allow removing lines
- `lineation.line.setarea` - Allow setting the area for a line
- `lineation.line.addborder` - Allow adding borders for a line
- `lineation.line.removeborder` - Allow removing borders for a line
- `lineation.line.addcheckpoint` - Allow adding checkpoints for a line
- `lineation.line.removecheckpoint` - Allow removing checkpoints for a line
- `lineation.line.tp` - Allow teleporting to a lines teleport point
- `lineation.line.start` - Allow starting lines
- `lineation.line.stop` - Allow stopping lines
- `lineation.line.link` - Allows linking lines
- `lineation.line.option.blocksequence` - Allow setting the block sequence for a line
- `lineation.line.option.teleport` - Allow setting a teleport location for a line
- `lineation.line.option.maxwinners` - Allow setting the max winners for a line
- `lineation.line.option.addcommand` - Allow adding commands to run at finish
- `lineation.line.option.removecommand` - Allow removing command to run
- `lineation.line.option.laps` - Allow setting the laps for a line
- `lineation.line.option.messagereach` - Allow setting the reach for the announcement messages
- `lineation.line.option.gamemodes` - Allow setting the allowed gamemodes for a line
- `lineation.line.option.illegalarea` - Allows using the illegal area option
- `lineation.line.option.illegalarea.add` - Allow adding illegal areas to a line
- `lineation.line.option.illegalarea.remove` - Allow removing set illegal areas

#### What's an area?

A line's area is used to detect players, and to know where the line is located.  
The center of the area is used to calculate the distance to a player, used in the messagereach option.  
In starting lines, it is possible to teleport all players in the area to a location when the line starts with the teleport option.  
In finish lines, this area is used to detect players. It's vital to set an area that players enter or cross through when they should finish.  
If your players are moving at a high speed, such as in elytra races, be sure to set a large area. If your server is lagging, not doing so could result in missed player wins.

#### What's a border?

You can set borders on lines, these are cuboid shapes where blocks will be set when the line is stopped.  
In finish lines, the block that is set when the line is stopped is defined in the block option.  
To change the block used, run /lineation line <line> option block <block>.  
In start lines, there is a countdown which is defined by the blocksequence option.  
For every block defined in the blocksequence option after the first (The resting position), the borders will take one second to start.  
By default this is set to "glass, red_stained_glass, yellow_stained_glass, lime_stained_glass", this creates a countdown of 3 seconds.

[Back to index](./index.html)  
