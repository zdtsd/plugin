package io.github.z.plugin.itemstats.attributes;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Attribute;
import org.bukkit.entity.Player;

public class Defense implements Attribute {

    private static final double defensePerLevel = 0.95;

    @Override
    public String getName() {
        return "defense";
    }


    @Override
    public void onHurt(Player player, DamageEvent event, double level){
        event.addDamageReduction(Math.pow(defensePerLevel, level));
    }
}
