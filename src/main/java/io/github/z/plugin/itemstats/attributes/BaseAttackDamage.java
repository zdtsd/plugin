package io.github.z.plugin.itemstats.attributes;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Attribute;
import io.github.z.plugin.utils.PlayerUtils;
import org.bukkit.entity.Player;

public class BaseAttackDamage implements Attribute {
    @Override
    public String getName() {
        return "attack_damage_base";
    }

    @Override
    public double getPriorityAmount() {
        return -100;
    }


    @Override
    public void onHurt(Player player, DamageEvent event, double level){
        if(event.getType() == DamageEvent.DamageType.MELEE_ATTACK){
            event.setBaseDamage(level * player.getCooledAttackStrength(0));
            event.setIsCrit(PlayerUtils.isFallingAttack(player));
        }
    }
}
