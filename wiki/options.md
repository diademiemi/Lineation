---
layout: default
---

# Options

## Both types

### link
This option will link this line with another line of the other type. With this, if one starts, the other will start too, the same goes for stopping.  

## Start lines

#### blocksequence
This option can be set to specify a custom sequence of blocks  
For both types of lines, the first block is used as a "resting" position, this is what the line gets set to when it stops. For finish lines, this is also the only block i
For start lines, you can set a comma seperated string of blocks like so:  
`/lineation line <name> option blocksequence glass,red_stained_glass,yellow_stained_glass,lime_stained_glass`  
Since this has 3 blocks after the resting position, this line will take 3 seconds to open. For every block added, the line will take one extra second to open.  

### teleport
This option will teleport players in the area to a certain location when the countdown is over.  
The option `here` sets the teleport point to your current location. Setting `disable` will disable the teleport.  

## Finish lines

#### blocksequence
For finish lines, this option will only use one block. This is the block it is set to when the line is stopped.  

#### teleport
This option teleports players who finish the line to a location.  
The option `here` sets the teleport point to your current location. Setting `disable` will disable the teleport.  

#### maxwinners
This option sets the amount of people that have to win before the line will automatically close.  
For example, if this is set to 3, the line will close after 3 people finish.  

#### laps
This option sets the amount of laps this line required players to do before finishing.  
This is set to 1 by default, multiple laps require at least one checkpoint to be set, with multiple around the course being recommended. Having 2 checkpoints means players can not go backwards, as the checkpoint will be subtracted if they pass through an earlier checkpoint.  

#### messagereach
This option sets who will receive announcement messages when the someone wins or when the line closes.  
Setting this to `all` will mean every online player receives the message, `world` will send this to every player in this world. `disabled` will disable these announcement messages.  

#### gamemodes
This is a comma seperated string of gamemodes players have to be in to finish.  
To set this to only allow survival and adventure mode, which is the default, run:  
`/lineation line <name> option gamemodes SURVIVAL,ADVENTURE`  

[Back to index](./index.html)  
