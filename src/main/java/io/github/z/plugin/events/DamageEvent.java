package io.github.z.plugin.events;

import io.github.z.plugin.itemstats.ItemStatManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;

public class DamageEvent extends Event implements Cancellable {

    public enum DamageType{
        MELEE_ATTACK,
        MELEE_SKILL,
        MELEE_ENCH,
        PROJ_ATTACK,
        PROJ_SKILL,
        PROJ_ENCH,
        MAGIC_ATTACK,
        MAGIC_SKILL,
        MAGIC_ENCH,
        THORNS,
        BLAST,
        FIRE,
        FALL,
        AILMENT,
        TRUE,
        OTHER;

        public static DamageType getType(DamageCause cause){
            return switch (cause){
                case WORLD_BORDER, CONTACT, MELTING, DROWNING, STARVATION, LIGHTNING, FALLING_BLOCK, CUSTOM, DRYOUT,
                        FREEZE, CRAMMING, SONIC_BOOM, SUFFOCATION -> OTHER;
                case ENTITY_ATTACK -> MELEE_ATTACK;
                case ENTITY_SWEEP_ATTACK -> MELEE_ENCH;
                case PROJECTILE -> PROJ_ATTACK;
                case DRAGON_BREATH, MAGIC -> MAGIC_ATTACK;
                case THORNS -> THORNS;
                case BLOCK_EXPLOSION, ENTITY_EXPLOSION -> BLAST;
                case FIRE, FIRE_TICK, HOT_FLOOR, LAVA, CAMPFIRE -> FIRE;
                case FALL, FLY_INTO_WALL -> FALL;
                case POISON, WITHER -> AILMENT;
                case VOID, KILL, SUICIDE -> TRUE;
                default -> {
                    Bukkit.getLogger().info("Unknown damage type detected!");
                    yield OTHER;
                }
            };
        }

        public static boolean isType(DamageCause cause, DamageType type){
            return type == getType(cause);
        }

        private static final Set<DamageType> environmentalDamage = EnumSet.of(FIRE, FALL);
        public static boolean isEnvironmental(DamageType type){
            return type.isEnvironmental();
        }
        public boolean isEnvironmental(){
            return environmentalDamage.contains(this);
        }
    }

    public static class Metadata{
        private DamageType mType;
        private final @Nullable ItemStatManager.PlayerItemStats mPlayerItemStats;
        private final @Nullable String mSourceName;

        public Metadata(DamageType type){
            this(type, null, null);
        }
        public Metadata(DamageType type, String sourceName, ItemStatManager.PlayerItemStats playerItemStats) {
            mType = type;
            mSourceName = sourceName;
            mPlayerItemStats = playerItemStats;
        }

        public DamageType getType() {
            return mType;
        }

        public void setType(DamageType type) {
            mType = type;
        }

        public @Nullable String getSourceName() {
            return mSourceName;
        }
    }
    private final @Nullable Entity mDamager;
    private final LivingEntity mDamagee;
    private final EntityDamageEvent mEvent;

    public DamageEvent(EntityDamageEvent event, LivingEntity damagee) {
        this(event, damagee, DamageType.getType(event.getCause()));
    }

    public DamageEvent(EntityDamageEvent event, LivingEntity damagee, DamageType type) {
        this(event, damagee, new Metadata(type));
    }
    public DamageEvent(EntityDamageEvent event, LivingEntity damagee, Metadata metadata){
        mDamager = event instanceof EntityDamageByEntityEvent entityDamageByEntityEvent ? entityDamageByEntityEvent.getDamager() : null;
        mDamagee =  damagee;
        mEvent = event;
    }




    private boolean isCancelled = false;
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }
    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }
    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList(){
        return HANDLERS;
    }

}
