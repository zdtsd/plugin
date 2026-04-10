package io.github.z.plugin.itemstats.attributes;


import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Attribute;
import io.github.z.plugin.itemstats.ItemStatUtils;
import io.github.z.plugin.listeners.DamageListener;
import io.github.z.plugin.utils.ShieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BaseShieldHealth implements Attribute {


    @Override
    public String getName() {
        return "shield_hp_base";
    }

    @Override
    public double getPriorityAmount() {
        return 5000;
    }


    //TODO: block damage dealt from in front of you instead of reading the shield stuff
    @Override
    public void onHurt(Entity entity, DamageEvent event, double level) {
        if (!event.isBlockedByShield() || event.isCancelled()) {
            return;
        }

        if (entity instanceof Player player) {
            ShieldUtils.damageShield(player, event.getDamage());
            DamageListener.onShieldBlockDamage(player, event);
        }
    }
}
