package io.github.z.plugin.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {
    private static List<Player> onlinePlayers = new ArrayList<>();

    public static void onLogin(Player player){
        onlinePlayers.add(player);
    }

    public static void onLogout(Player player){
        onlinePlayers.remove(player);
    }

    public static List<Player> getOnlinePlayers(){
        return onlinePlayers;
    }
}
