package io.github.z.plugin.itemstats.enchantments;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Enchantment;
import org.bukkit.entity.Player;

public class FireProtection implements Enchantment {
    private static final double defensePerLevel = 0.9;

    @Override
    public String getName() {
        return "fire_protection";
    }


    @Override
    public void onHurt(Player player, DamageEvent event, double level){
        if(event.isType(DamageEvent.DamageType.FIRE)){
            event.addDamageReduction(Math.pow(defensePerLevel, level));
        }
    }
}
