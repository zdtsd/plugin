package io.github.z.plugin.itemstats.enchantments;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Enchantment;
import org.bukkit.entity.Entity;

public class DoubleDamage implements Enchantment {
    @Override
    public String getName() {
        return "DoubleDamage";
    }

    @Override
    public void onDamage(Entity entity, DamageEvent event, double level) {
        event.addMultiplicativeModifier(2.0);
    }
}
