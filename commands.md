---
layout: default
---

# Commands & Permissions

#### Global commands
 - `/lineation help` - Show global commands  
 - `/lineation help lines` - Shows commands relating to lines  
 - `/lineation help options` - Shows more advanced options for lines  
 - `/lineation config reload` - Saves and reloads config files  
 - `/lineation config maxwins <number>` - Sets the maximum amount of times someone can win  
 - `/lineation config forget <uuid/username>` - Forgets a players wins for maxwins  
 - `/lineation line create <type> <name>` - Create a line with a specified type and name  
 - `/lineation line remove <name>` - Remove a line by name
 - `/lineation line list <type>` - Lists all lines, type is optional
#### Line specific commands
 - `/lineation line <name> info` - Gets information about specified line  
 - `/lineation line <name> start` - Starts and opens this line  
 - `/lineation line <name> stop` - Stops and closes this line  
 - `/lineation line <name> setarea` - Sets the detection area for this line using your current WorldEdit selection  
 (This sets the detection area used to send the countdown for start lines and to count players for finish lines)  
 - `/lineation line <name> addborder` - Adds a border to set blocks at for this line using your current WorldEdit selection  
 (This adds a border which is used to set blocks with WorldEdit)  
 - `/lineation line <name> removeborder <number>` - Removes a border by number  
 - `/lineation line <name> addcheckpoint` - Adds a checkpoint players have to cross first before being counted to win  
 (Finish line only. Multiple checkpoints can be set, players have to pass through them in order)  
 - `/lieation line <name> removecheckpoint` - Removes a checkpoint by number  
 - `/lineation line <name> getwinners` - Gets the last winners of this line  
#### Line options
 - `/lineation line <name> option blocksequence <block1,block2,block3...>` - Comma seperated list of blocks to use for the opening sequence  
 (The first block is used when stopped. Only one is required. For every block added the countdown takes one second longer.)  
 - `/lineation line <name> option teleport <here/disable>` - Makes players teleport to this current location when a start line opens or when they finish   
 - `/lineation line <name> option maxwinners <number>` - Number of players that have to finish before the line closes  
 - `/lineation line <name> option addcommand <command>` - Sets a command to run when a player finishes. Placeholders: %player%, %uuid%  
 - `/lineation line <name> option removecommand <number>` - Removes a command by number  
 - `/lineation line <name> option laps <number>` - Amount of times players have to go through the course before winning  
 (This requires at least one checkpoint to be set, place them appropriately around your course)  
 - `/lineation line <name> option messagereach <all/world/disabled>` - Sets who the announcement messages will target  
 - `/ineation line <name> option gamemodes <survival,adventure>` - Comma seperated list of gamemodes players have to be in to be counted  
 - `/lineation line <name> option link <line name>` - This links a line of the other type to automatically start or stop at the same time  

### Permissions
 - `lineation.help` - Allow using the help commands
 - `lineation.reload` - Allow reloading the plugin config files
 - `lineation.maxwins` - Allow setting maxwins
 - `lineation.forget` - Allow forgetting a players wins
 - `lineation.line.list` - Allow seeing the list of lines
 - `lineation.line.create` - Allow creating new lines
 - `lineation.line.remove` - Allow removing lines
 - `lineation.line.setarea` - Allow setting the area for a line
 - `lineation.line.addborder` - Allow adding borders for a line
 - `lineation.line.removeborder` - Allow removing borders for a line
 - `lineation.line.addcheckpoint` - Allow adding checkpoints for a line
 - `lineation.line.removecheckpoint` - Allow removing checkpoints for a line
 - `lineation.line.start` - Allow starting lines
 - `lineation.line.stop` - Allow stopping lines
 - `lineation.line.option.blocksequence` - Allow setting the block sequence for a line
 - `lineation.line.option.teleport` - Allow setting a teleport location for a line
 - `lineation.line.option.maxwinners` - Allow setting the max winners for a line
 - `lineation.line.option.addcommand` - Allow adding commands to run at finish  
 - `lineation.line.option.removecommand` - Allow removing command to run  
 - `lineation.line.option.laps` - Allow setting the laps for a line
 - `lineation.line.option.messagereach` - Allow setting the reach for the announcement messages
 - `lineation.line.option.gamemodes` - Allow setting the allowed gamemodes for a line
 - `lineation.line.option.link` - Allows linking lines
