package io.github.z.plugin.abilities.swashbuckler;

import io.github.z.plugin.Plugin;
import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.abilities.CooldownAbility;
import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.effects.FallSpeedEffect;
import io.github.z.plugin.effects.StunEffect;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.particles.CircleParticleSet;
import io.github.z.plugin.utils.DamageUtils;
import io.github.z.plugin.utils.EntityUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collection;

public class SwashbucklerBladeDance extends CooldownAbility {
    public static AbilityData<SwashbucklerBladeDance> DATA = new AbilityData<>(SwashbucklerBladeDance.class, "Blade Dance", SwashbucklerBladeDance::new)
            .scoreboardID("SwshDance")
            .displayMaterial(Material.IRON_SWORD)
            .cooldown(3 * 20);


    private static final double mTickDamage = 1, mFinalDamage = 8, mRadius = 4, mTickHitUpSpeed = 0.2;
    private static final int mTicksPerHit = 3, mHits = 6, mStunDuration = 2 * 20, mDuration = mTicksPerHit * mHits;
    private static final String mStunEffectName = "SwshDanceStun", mSlowFallEffectName = "SwshDanceFall";
    private static final Particle mParticle = Particle.SWEEP_ATTACK;
    private static final int mParticleCount = 10;
    private BladeDanceInstance mInstance;
    SwashbucklerStance playerStance;
    private SwashbucklerStance getOrFetchStance(){
        if(playerStance == null){
            playerStance = (SwashbucklerStance) AbilityManager.getAbility(mPlayer, SwashbucklerStance.class);
        }
        return playerStance;
    }


    @Override
    public void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        if(!isCastable()){
            return;
        }
        //Don't cast if already active (unlikely to ever happen)
        if(mInstance != null){
            return;
        }
        spendCharge();
        mInstance = new BladeDanceInstance();
    }


    @Override
    public void onHurt(DamageEvent event) {
        if(mInstance != null && !event.isType(DamageEvent.DamageType.TRUE)){
            event.setCancelled(true);
        }
    }

    private class BladeDanceInstance{

        private int hitsDone = 0;
        public BladeDanceInstance(){
            danceStart();
        }
        private void danceStart(){
            mPlayer.getWorld().playSound(mPlayer.getLocation(), Sound.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0f, 1.0f);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), this::danceTick, mTicksPerHit);
        }

        private void danceTick(){
            hitsDone++;
            if(hitsDone < mHits){
                Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), this::danceTick, mTicksPerHit);
                mPlayer.getWorld().playSound(mPlayer.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.7f, 01.0f);
                for(Entity entity : EntityUtils.getEntitiesInSphere(mPlayer.getLocation(), mRadius)){
                    if(entity instanceof LivingEntity le && !(entity instanceof Player)){
                        DamageUtils.damage(mPlayer, le, DamageEvent.DamageType.MELEE_SKILL, mTickDamage, DATA.getName(), true, false);
                        EntityUtils.setVelocityY(le, mTickHitUpSpeed);
                        EffectManager.applyEffect(le, new FallSpeedEffect(3, -80), mSlowFallEffectName);
                    }
                }
                new CircleParticleSet()
                        .setCenter(mPlayer.getLocation())
                        .setRadius(mRadius)
                        .setParticle(mParticle)
                        .setCount(mParticleCount)
                        .generateParticles();
            }
            else{
                danceEnd();
            }
        }

        private void danceEnd(){
            mPlayer.getWorld().playSound(mPlayer.getLocation(), Sound.ITEM_SHIELD_BREAK, SoundCategory.PLAYERS, 1.0f, 0.8f);
            Collection<Entity> nearbyEntities = EntityUtils.getEntitiesInSphere(mPlayer.getLocation(), mRadius);
            SwashbucklerStance.SwashbucklerStanceType currentStance = getOrFetchStance().getStance();
            for(Entity entity : nearbyEntities){
                if(entity instanceof LivingEntity le && !(entity instanceof Player)){
                    DamageUtils.damage(mPlayer, le, DamageEvent.DamageType.MELEE_SKILL, mFinalDamage, DATA.getName(), true, false);
                    if(currentStance == SwashbucklerStance.SwashbucklerStanceType.OFFENSIVE){
                        EffectManager.applyEffect(le, new StunEffect(mStunDuration, 1), mStunEffectName);
                    }
                }
            }
            if(currentStance == SwashbucklerStance.SwashbucklerStanceType.OFFENSIVE || currentStance == SwashbucklerStance.SwashbucklerStanceType.NEUTRAL){
                getOrFetchStance().setStance(SwashbucklerStance.SwashbucklerStanceType.DEFENSIVE);
            }
            mInstance = null;
        }
    }

    public SwashbucklerBladeDance(Player mPlayer) {
        super(mPlayer);
    }
    @Override
    public AbilityData<?> getData() {
        return DATA;
    }
}
