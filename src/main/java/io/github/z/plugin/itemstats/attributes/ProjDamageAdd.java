package io.github.z.plugin.itemstats.attributes;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Attribute;
import org.bukkit.entity.Entity;

public class ProjDamageAdd implements Attribute {
    @Override
    public String getName() {
        return "proj_damage_add";
    }

    @Override
    public void onDamage(Entity entity, DamageEvent event, double level){
        if(event.getType() == DamageEvent.DamageType.PROJ_ATTACK){
            event.addBaseDamage(level);
        }
    }
}
