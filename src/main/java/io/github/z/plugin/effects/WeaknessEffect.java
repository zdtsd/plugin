package io.github.z.plugin.effects;

import io.github.z.plugin.events.DamageEvent;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class WeaknessEffect extends Effect {

    public WeaknessEffect(int duration, int strength) {
        super(duration, strength);
    }

    @Override
    public void onHurt(Entity entity, DamageEvent event, double level) {
        event.addDamageReduction(1 - (level/100.0) );
    }

    @Override
    public List<String> getSidebarLines() {
        List<String> lines = new ArrayList<>();
        lines.add(getName() + " " + getStrength() + "||" + getDuration());
        return lines;
    }


    @Override
    public String getName() {
        return "Weakness";
    }
}
