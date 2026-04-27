package io.github.z.plugin.utils;

import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.itemstats.ItemStatManager;
import io.github.z.plugin.itemstats.ItemStatUtils;
import net.kyori.adventure.util.TriState;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {
    private static List<Player> onlinePlayers = new ArrayList<>();

    public static void onLogin(Player player){
        onlinePlayers.add(player);
        AbilityManager.getAbilityManager().updateAbilities(player);

        //TODO: Only do this if server has reset since player last login.
        removeAllAttributeModifiers(player);

        //Ensure that fall damage is dealt to players
        player.setFlyingFallDamage(TriState.TRUE);

        //Load equipment stats
        ItemStatManager.updateStats(player);
    }

    public static void removeAllAttributeModifiers(Player player){
        for(Attribute attribute : Attribute.values()){
            if(player.getAttribute(attribute) != null){
                for(AttributeModifier attributeModifier : player.getAttribute(attribute).getModifiers()){
                    player.getAttribute(attribute).removeModifier(attributeModifier);
                }
            }

        }
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
