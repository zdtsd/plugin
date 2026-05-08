package io.github.z.plugin.itemstats.enchantments;

import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.effects.effects.StunEffect;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Enchantment;
import org.bukkit.entity.Entity;

public class ThunderAspect implements Enchantment {

    private static final double oddsPerLevel = 0.2;
    private static int duration = 1 * 20;
    private static int stunLevel = 1;
    @Override
    public String getName() {
        return "thunder_aspect";
    }

    @Override
    public void onDamage(Entity entity, DamageEvent event, double level) {
        if(event.doesDamageTriggerAspect()){
            if(Math.random() < oddsPerLevel * level){
                EffectManager.applyEffect(event.getDamagee(), new StunEffect(duration, stunLevel), StunEffect.stunNamespace);
            }
        }
    }
}
