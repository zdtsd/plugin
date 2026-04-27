package io.github.z.plugin;

import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.cutscenes.CutsceneManager;
import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.itemstats.ItemStatManager;
import io.github.z.plugin.mobspells.MobSpellManager;
import io.github.z.plugin.sidebar.SidebarManager;
import io.github.z.plugin.utils.PlayerUtils;
import io.github.z.plugin.utils.TABUtils;
import me.neznamy.tab.api.TabPlayer;
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
        CutsceneManager.tick();
        boolean fourHz = mCounter % 5 == 0;
        boolean twoHz = mCounter % 10 == 0;
        boolean oneHz = mCounter % 20 == 0;

        if(fourHz){
            for(Player player : PlayerUtils.getOnlinePlayers()){
                //ItemStatManager.updateStats(player);
                ItemStatManager.tick(player, twoHz, oneHz);
                AbilityManager.tick(player, twoHz, oneHz);
            }
            MobSpellManager.tick(twoHz, oneHz);
            for(TabPlayer tabPlayer : TABUtils.getTABPlayers()){
                SidebarManager.getInstance().tick(tabPlayer);
            }
            EffectManager.getEffectManager().tick();
        }
    }
}
