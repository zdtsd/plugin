package io.github.z.plugin.utils;

import io.github.z.plugin.events.DamageEvent;
import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Objects;

public class DamageUtils {
    public static DamageEvent.Metadata nextMetadata;

    public static void damage(LivingEntity damager, LivingEntity damagee, DamageEvent.DamageType type, double amount){
        damage(damager, damagee, type, amount, null, false);
    }

    public static void damage(LivingEntity damager, LivingEntity damagee, DamageEvent.DamageType type, double amount,
                              String sourceName, boolean ignoreIframes){
        damage(damager, damagee, type, amount, sourceName, ignoreIframes, false);
    }

    public static void damage(LivingEntity damager, LivingEntity damagee, DamageEvent.DamageType type, double amount,
                              String sourceName, boolean ignoreIframes, boolean knockback) {
        damage(damager, damagee, new DamageEvent.Metadata(type, sourceName), amount,
                ignoreIframes, knockback, true);
    }
    //TODO: Deal knockback
    public static void damage(LivingEntity damager, LivingEntity damagee, DamageEvent.Metadata metadata,
                              double amount, boolean ignoreIframes, boolean knockback, boolean shieldBlockable){
        if(amount < 0){
            amount = 0;
            return;
        }
        if(!damagee.isValid()){
            return;
        }

        nextMetadata = metadata;
        int iFrames = damagee.getNoDamageTicks();
        Vector velocity = damagee.getVelocity();
        if(ignoreIframes){
            damagee.setNoDamageTicks(0);
        }

        damagee.damage(amount, damager);

        if(ignoreIframes){
            damagee.setNoDamageTicks(iFrames);
        }
        if(!knockback){
            damagee.setVelocity(velocity);
        }

    }

    //TODO: Set up an event structure for interception purposes.
    public static double healEntity(LivingEntity entity, double amount){
        double amountHealed = Math.min(amount, Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue() - entity.getHealth());
        entity.heal(amount);
        return amountHealed;
    }


}
