package io.github.z.plugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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

    public static void setVelocityY(Entity entity, double velocityY){entity.setVelocity(entity.getVelocity().setY(velocityY));}

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

    public static Location getEntityCenter(Entity entity){
        return new Location(entity.getWorld(), entity.getBoundingBox().getCenterX(), entity.getBoundingBox().getCenterY(), entity.getBoundingBox().getCenterZ());
    }

    public static Collection<Entity> getEntitiesInSphere(Location location, double range){
        Collection<Entity> nearbyEntities = location.getNearbyEntities(range, range, range);
        nearbyEntities.removeIf(entity -> entity.getLocation().distance(location) > range);
        return nearbyEntities;
    }

    public static Collection<Entity> getEntitiesInCone(Location location, double range, double degrees){
        Collection<Entity> nearbyEntities = getEntitiesInSphere(location, range);
        nearbyEntities.removeIf(entity -> {
            for(Vector vector : getEntityBoundingBoxVertices(entity)){
                if(vector.clone().subtract(location.toVector()).normalize().dot(location.getDirection()) >= Math.cos(degrees / 2)){
                    return false;
                }
            }
            return true;
        });
        return nearbyEntities;
    }

    public static List<Vector> getEntityBoundingBoxVertices(Entity entity){
        List<Vector> vertices = new ArrayList<>();
        BoundingBox box = entity.getBoundingBox();
        boolean[] options = new boolean[2];
        options[0] = true;
        options[1] = false;
        for(boolean x : options){
            for(boolean y : options){
                for (boolean z : options){
                    double posX = x ? box.getMinX() : box.getMaxX();
                    double posY = y ? box.getMinY() : box.getMaxY();
                    double posZ = z ? box.getMinZ() : box.getMaxZ();
                    vertices.add(new Vector(posX, posY, posZ));
                }
            }
        }
        vertices.add(entity.getBoundingBox().getCenter());
        return vertices;
    }


}
