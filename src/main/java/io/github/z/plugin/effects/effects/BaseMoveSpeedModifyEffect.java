package io.github.z.plugin.effects.effects;

import io.github.z.plugin.Plugin;
import io.github.z.plugin.effects.Effect;
import io.github.z.plugin.utils.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class BaseMoveSpeedModifyEffect extends Effect {

    private NamespacedKey key = new NamespacedKey(Plugin.getPlugin(), "MoveSpdMod");

    public BaseMoveSpeedModifyEffect(int duration, int strength) {
        super(duration, strength);
        mStrength = Math.max(-100, strength);
    }

    public boolean isSlow(){
        return mStrength < 0;
    }

    @Override
    public String getName() {
        return "Movement Speed Modifier";
    }

    @Override
    public List<String> getSidebarLines() {
        List<String> lines = new ArrayList<>();
        lines.add(getName() + " " + getStrength() + "||" + getDuration());
        return lines;
    }

    @Override
    public void onEffectRemove(Effect newEffect, Entity entity) {
        if(entity instanceof Attributable attributable){
            EntityUtils.removeAttribute(attributable, Attribute.GENERIC_MOVEMENT_SPEED, key);
        }
    }

    @Override
    public void onEffectAdd(Effect oldEffect, Entity entity) {
        if(entity instanceof Attributable attributable){
            EntityUtils.replaceAttribute(attributable, Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(key, (double)mStrength * 0.01, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        }
    }

}
