package io.github.z.plugin.itemstats.attributes;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Attribute;
import org.bukkit.entity.Player;

public class PercentProjDamage implements Attribute {
    @Override
    public String getName() {
        return "proj_damage_percent";
    }

    @Override
    public void onHurt(Player player, DamageEvent event, double level){
        if(event.isProjectile()){
            event.addGearMultiplier(1 + level / 100);
        }
    }
}
