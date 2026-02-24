package io.github.z.plugin.sidebar;

import io.github.z.plugin.Plugin;
import io.github.z.plugin.abilities.AbilityManager;
import it.unimi.dsi.fastutil.Hash;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.scoreboard.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class SidebarManager {

    private static SidebarManager instance;

    public SidebarManager(){
        instance = this;
    }

    public static SidebarManager getInstance(){
        return instance;
    }



    private static final String sidebarName = "sidebarScoreboard";
    private static final String sidebarTitle = "Sidebar Scoreboard";
    public void tick(TabPlayer player){
        renderSidebar(player);
    }

    private boolean flipflop = true;
    private void renderSidebar(TabPlayer player){
        //TODO: REFACTOR TO FIX SIDEBAR FLICKERING
        //Get ability lines
        List<String> sidebarLines = new ArrayList<>();
        sidebarLines.addAll(AbilityManager.getAbilityManager().getSidebarLines((Player) player.getPlayer()));

        //Show sidebar
        Scoreboard sidebar = TabAPI.getInstance().getScoreboardManager().createScoreboard(sidebarName, sidebarTitle, sidebarLines);
        TabAPI.getInstance().getScoreboardManager().showScoreboard(player, sidebar);
    }

}
