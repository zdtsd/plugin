package io.github.z.plugin.itemstats.attributes;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Attribute;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MeleeDamageAdd implements Attribute {
    @Override
    public String getName() {
        return "melee_damage";
    }

    @Override
    public void onDamage(Player player, DamageEvent event, double level){
        if(event.getType() == DamageEvent.DamageType.MELEE_ATTACK){
            event.addBaseDamage(level);
        }
    }
}
