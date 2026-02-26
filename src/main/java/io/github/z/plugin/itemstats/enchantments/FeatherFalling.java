package io.github.z.plugin.itemstats.enchantments;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Enchantment;
import org.bukkit.entity.Entity;

public class FeatherFalling implements Enchantment {
    private static final double defensePerLevel = 0.8;

    @Override
    public String getName() {
        return "fall_protection";
    }


    @Override
    public void onHurt(Entity entity, DamageEvent event, double level){
        if(event.isType(DamageEvent.DamageType.FALL)){
            event.addDamageReduction(Math.pow(defensePerLevel, level));
        }
    }
}
