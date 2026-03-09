package io.github.z.plugin.effects;

import io.github.z.plugin.Plugin;
import io.github.z.plugin.utils.EntityUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;

public class FallSpeedEffect extends Effect{
    public FallSpeedEffect(int duration, int strength) {
        super(duration, strength);
    }


    private NamespacedKey key = new NamespacedKey(Plugin.getPlugin(), "FallSpdMod");
    @Override
    public String getName() {
        return "Slow Falling";
    }

    @Override
    public void onEffectRemove(Effect newEffect, Entity entity) {
        if(entity instanceof Attributable attributable){
            EntityUtils.removeAttribute(attributable, Attribute.GENERIC_GRAVITY, key);
        }
    }

    @Override
    public void onEffectAdd(Effect oldEffect, Entity entity) {
        if(entity instanceof Attributable attributable){
            EntityUtils.replaceAttribute(attributable, Attribute.GENERIC_GRAVITY, new AttributeModifier(key, (double)mStrength * 0.01, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        }
    }
}
