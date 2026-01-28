package io.github.z.plugin.utils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

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

    public static boolean isFallingAttack(Player player){
        return player.getCooledAttackStrength(0.5f) > 0.9
                && player.getFallDistance() > 0
                && !player.hasPotionEffect(PotionEffectType.BLINDNESS)
                && isMidair(player);
    }

    public static boolean isMidair(Player player){
        if(!player.isOnGround() && player.getLocation().isChunkLoaded()){
            //TODO: Check if player is climbing a climbable block.
            return true;
        }
        return false;
    }
}
