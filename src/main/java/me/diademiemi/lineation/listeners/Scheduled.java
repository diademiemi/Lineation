package me.diademiemi.lineation.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.diademiemi.lineation.Lineation;

/**
 * Checks player locations per tick
 * 
 * @author diademiemi
 */
public class Scheduled {

    public static void cancelTasks(Lineation plugin) {
        Bukkit.getServer().getScheduler().cancelTasks(plugin);
    }

    /**
     * Schedules movement check every tick
     */
    public static void checkMovements() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Lineation.getInstance(), new Runnable(){
        public void run() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                  Checker.checkPlayer(p);
              }
          }
      },0, 1);
  }

}