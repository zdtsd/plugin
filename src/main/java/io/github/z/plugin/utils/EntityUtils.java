package io.github.z.plugin.utils;

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
}
