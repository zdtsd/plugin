package io.github.z.plugin.effects.effects;

import io.github.z.plugin.effects.Effect;
import io.github.z.plugin.events.DamageEvent;
import org.bukkit.entity.Entity;

public class FeatherFallingEffect extends Effect {
    public FeatherFallingEffect(int duration, int strength) {
        super(duration, strength);
    }

    @Override
    public String getName() {
        return "Feather Falling";
    }

    @Override
    public void onHurt(Entity entity, DamageEvent event, double level) {
        if (event.isType(DamageEvent.DamageType.FALL)) {
            event.setCancelled(true);
        }
    }
}
