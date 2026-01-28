package io.github.z.plugin.events;

import io.github.z.plugin.itemstats.ItemStatManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.projectiles.ProjectileSource;
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

        private static final Set<DamageType> meleeDamage = EnumSet.of(MELEE_ATTACK, MELEE_SKILL, MELEE_ENCH);
        public static boolean isMelee(DamageType type){
            return type.isMelee();
        }

        public boolean isMelee(){
            return meleeDamage.contains(this);
        }

        private static final Set<DamageType> projectileDamage = EnumSet.of(PROJ_ATTACK, PROJ_SKILL, PROJ_ENCH);
        public static boolean isProjectile(DamageType type){
            return type.isProjectile();
        }

        public boolean isProjectile(){
            return projectileDamage.contains(this);
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
    private final @Nullable Entity mSource;
    private final EntityDamageEvent mEvent;
    private final double mOriginalDamage;
    private final Metadata mMetadata;


    private double mBaseDamage;
    private double mDamageMultiplier = 1;
    private double mGearDamageMultiplier = 1;
    private double mDamageResistanceDivisor = 1;
    private boolean mIsCrit = false;

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
        mOriginalDamage = event.getDamage();
        mBaseDamage = mOriginalDamage;
        mMetadata = metadata;
        //Set source of indirect damage
        if(mDamager instanceof Projectile projectile){
            mSource = projectile.getShooter() instanceof Entity e ? e : null;
        }
        else if (mDamager instanceof EvokerFangs evokerFangs){
            mSource = evokerFangs.getOwner() instanceof Entity e ? e : null;
        }
        else{
            mSource = mDamager;
        }
    }



    public void addGearMultiplier(double gearMult){
        mGearDamageMultiplier += gearMult - 1;
        recalculateDamage();
    }

    public void addBaseDamage(double baseBonus){
        mBaseDamage += baseBonus;
        recalculateDamage();
    }
    public void setBaseDamage(double baseDamage){
        mBaseDamage = baseDamage;
    }
    public void addDamageMultiplier(double mult){
        mDamageMultiplier += mult - 1;
        recalculateDamage();
    }
    public void addDamageReduction(double reduction){
        mDamageResistanceDivisor += reduction - 1;
        recalculateDamage();
    }

    private static final double MAX_DAMAGE = 100000;
    private void recalculateDamage(){
        double damage = Math.max((mBaseDamage * mGearDamageMultiplier * mDamageMultiplier * (1/mDamageResistanceDivisor) * critModifier()), 0);
        if(mEvent.getCause() == DamageCause.POISON && damage >= mDamagee.getHealth()){
            damage = mDamagee.getHealth() - 1;
        }
        mEvent.setDamage(damage);

        Bukkit.getLogger().info("Damage recalculated to: " + damage);
    }

    public void setIsCrit(boolean crit) {
        mIsCrit = crit;
        recalculateDamage();
    }

    private double critModifier(){
        return mIsCrit ? 1.5 : 1.0;
    }

    public double getDamage(){
        return mEvent.getDamage();
    }

    public Entity getSource(){
        return mSource;
    }
    public LivingEntity getDamagee(){
        return mDamagee;
    }
    public Entity getDamager(){
        return mDamager;
    }


    public boolean isMelee(){
        return mMetadata.mType.isMelee();
    }
    public boolean isProjectile(){
        return mMetadata.mType.isProjectile();
    }
    public boolean isEnvironmental(){
        return mMetadata.mType.isEnvironmental();
    }

    public DamageType getType(){
        return mMetadata.mType;
    }

    @Override
    public boolean isCancelled() {
        return mEvent.isCancelled();
    }
    @Override
    public void setCancelled(boolean b) {mEvent.setCancelled(b);}
    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList(){
        return HANDLERS;
    }

}
