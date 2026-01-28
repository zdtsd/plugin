package io.github.z.plugin;

import io.github.z.plugin.itemstats.ItemStatManager;
import io.github.z.plugin.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Timer {

    private static Plugin mPlugin;

    public static void setPlugin(Plugin p){
        mPlugin = p;
        Bukkit.getScheduler().scheduleSyncDelayedTask(mPlugin, Timer::tick, 1);
    }
    private static int mCounter = 0;
    private static void tick(){
        Bukkit.getScheduler().scheduleSyncDelayedTask(mPlugin, Timer::tick, 1);

        mCounter++;
        boolean fourHz = mCounter % 5 == 0;
        boolean twoHz = mCounter % 10 == 0;
        boolean oneHz = mCounter % 20 == 0;

        if(fourHz){
            for(Player player : PlayerUtils.getOnlinePlayers()){
                Bukkit.getLogger().info("4Hz");
                ItemStatManager.updateStats(player);
                ItemStatManager.tick(mPlugin, player, twoHz, oneHz);
            }
        }
    }
}
