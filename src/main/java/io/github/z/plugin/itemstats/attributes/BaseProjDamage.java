package io.github.z.plugin.itemstats.attributes;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Attribute;
import io.github.z.plugin.utils.PlayerUtils;
import org.bukkit.entity.Player;

public class BaseProjDamage implements Attribute {
    @Override
    public String getName() {
        return "proj_damage_base";
    }

    @Override
    public double getPriorityAmount() {
        return -100;
    }


    //TODO: Track base damage of arrow when shot rather than on hit.
    @Override
    public void onDamage(Player player, DamageEvent event, double level){
        if(event.getType() == DamageEvent.DamageType.PROJ_ATTACK){
            event.setBaseDamage(level);
        }
    }
}
