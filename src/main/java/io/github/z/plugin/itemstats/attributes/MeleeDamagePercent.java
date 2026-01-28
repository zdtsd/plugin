package io.github.z.plugin.itemstats.attributes;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Attribute;
import org.bukkit.entity.Player;

public class MeleeDamagePercent implements Attribute {
    @Override
    public String getName() {
        return "melee_damage_percent";
    }

    @Override
    public void onHurt(Player player, DamageEvent event, double level){
        if(event.isMelee()){
            event.addGearMultiplier(1 + level / 100);
        }
    }
}
