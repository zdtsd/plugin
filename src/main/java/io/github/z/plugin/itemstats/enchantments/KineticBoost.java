package io.github.z.plugin.itemstats.enchantments;

import io.github.z.plugin.effects.effects.AgilityEffect;
import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class KineticBoost implements Enchantment {

    @Override
    public String getName() {
        return "kinetic_boost";
    }

    @Override
    public void onKill(Entity killer, DamageEvent event, LivingEntity damagee, double level) {
        if (killer instanceof Player player) {
            EffectManager.applyEffect(player, new AgilityEffect(40, 2), "kinetic_boost");
        }
    }
}
