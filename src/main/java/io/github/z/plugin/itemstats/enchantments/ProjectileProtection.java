package io.github.z.plugin.itemstats.enchantments;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Enchantment;
import org.bukkit.entity.Player;

public class ProjectileProtection implements Enchantment {
    private static final double defensePerLevel = 0.9;

    @Override
    public String getName() {
        return "proj_protection";
    }


    @Override
    public void onHurt(Player player, DamageEvent event, double level){
        if(event.isProjectile()){
            event.addDamageReduction(Math.pow(defensePerLevel, level));
        }
    }
}
