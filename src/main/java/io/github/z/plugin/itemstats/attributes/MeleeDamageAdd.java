package io.github.z.plugin.itemstats.attributes;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Attribute;
import org.bukkit.entity.Entity;

public class MeleeDamageAdd implements Attribute {
    @Override
    public String getName() {
        return "melee_damage";
    }

    @Override
    public void onDamage(Entity entity, DamageEvent event, double level){
        if(event.getType() == DamageEvent.DamageType.MELEE_ATTACK){
            event.addBaseDamage(level);
        }
    }
}
