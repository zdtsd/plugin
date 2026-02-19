package io.github.z.plugin.utils;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TABUtils {

    public static TabPlayer[] getTABPlayers(){
        return TabAPI.getInstance().getOnlinePlayers();
    }

}
