package io.github.z.plugin.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import javax.xml.stream.Location;

public class EntityUtils {

    public static void addVelocity(Entity entity, Vector velocity){
        entity.setVelocity(entity.getVelocity().add(velocity));
    }

    public static void addForwardsVelocity(Entity entity, double speed){
        addVelocity(entity, entity.getLocation().getDirection().multiply(speed));
    }

    public static void setVelocity(Entity entity, Vector velocity){
        entity.setVelocity(velocity);
    }

    public static void setForwardsVelocity(Entity entity, double speed){
        setVelocity(entity, entity.getLocation().getDirection().multiply(speed));
    }

    public static void replaceAttribute(Attributable attributable, Attribute attribute, AttributeModifier attributeModifier){
        removeAttribute(attributable, attribute, attributeModifier.getKey());
        addAttribute(attributable, attribute, attributeModifier);
    }

    public static void addAttribute(Attributable attributable, Attribute attribute, AttributeModifier modifier) {
        AttributeInstance instance = attributable.getAttribute(attribute);
        if (instance != null) {
            instance.addModifier(modifier);
        }
    }

    public static void removeAttribute(Attributable attributable, Attribute attribute, NamespacedKey modifierName) {
        AttributeInstance instance = attributable.getAttribute(attribute);
        if (instance != null) {
            for (AttributeModifier modifier : instance.getModifiers()) {
                if (modifier != null && modifier.getKey().equals(modifierName)) {
                    instance.removeModifier(modifier);
                }
            }
        }
    }
}
