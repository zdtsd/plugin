package io.github.z.plugin.abilities.stormcaller;

import io.github.z.plugin.Plugin;
import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.abilities.CooldownAbility;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.particles.ArcParticleSet;
import io.github.z.plugin.utils.DamageUtils;
import io.github.z.plugin.utils.EntityUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.util.Vector;

import java.util.Collection;

public class StormcallerStrikeTwice extends CooldownAbility {

    public static AbilityData<StormcallerStrikeTwice> DATA = new AbilityData<>(StormcallerStrikeTwice.class, "Strike Twice", StormcallerStrikeTwice::new)
            .scoreboardID("StrmStrike")
            .displayMaterial(Material.IRON_SWORD)
            .cooldown(3 * 20)
            .maxCharges(1);

    private boolean mEmpowered;
    private StormcallerStaticCharge mStaticCharge;

    private static final int ticksPerSlash = 5;

    private static final double attackRange = 6, spinAttackRange = 10, attackAngle = 120, attackDamage = 6;

    public StormcallerStrikeTwice(Player player) {
        super(player);
    }

    @Override
    public AbilityData<?> getData() {
        return DATA;
    }

    private StormcallerStaticCharge getOrFetchStaticCharge() {
        if (mStaticCharge == null) {
            mStaticCharge = (StormcallerStaticCharge) AbilityManager.getAbility(mPlayer, StormcallerStaticCharge.class);
        }
        return mStaticCharge;
    }

    @Override
    public void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        if (!isCastable()) return;
        spendCharge();
        mEmpowered = getOrFetchStaticCharge().onCastNoIncrement();

        if(!mEmpowered){
            doSlash(false, false, false);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), () -> {doSlash(true, false, false);}, ticksPerSlash);
        }
        else{
            doSlash(false, true, false);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), () -> {doSlash(true, true, false);}, ticksPerSlash);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), () -> {doSlash(false, true, false);}, ticksPerSlash*2);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), () -> {doSlash(true, true, false);}, ticksPerSlash*3);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), () -> {doSlash(false, true, true);}, ticksPerSlash*4);
        }
    }

    private void doSlash(boolean invert, boolean empowered, boolean finalStrike){
        double angleMult = invert ? -1 : 1;
        Vector side = mPlayer.getEyeLocation().getDirection();
        side.setY(0);
        side.rotateAroundY(Math.toRadians(90));
        Vector v = mPlayer.getEyeLocation().getDirection();
        v.rotateAroundNonUnitAxis(side, Math.toRadians(90));
        if(!finalStrike){
            v.rotateAroundNonUnitAxis(mPlayer.getEyeLocation().getDirection(), Math.toRadians(45 * angleMult));
            new ArcParticleSet()
                    .setParticle(Particle.END_ROD)
                    .setArcWidth(10)
                    .setArcLength(18)
                    .setDuration(ticksPerSlash)
                    .setArcAngle(attackAngle * angleMult)
                    .setCenter(mPlayer.getEyeLocation().subtract(0, 0.2, 0).subtract(side.normalize().multiply(-0.9)))
                    .setAxis(v)
                    .setInnerRadius(3)
                    .setOuterRadius(attackRange)
                    .generateParticles();
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), () -> {slashDamage(empowered, false);}, ticksPerSlash / 2);
            mPlayer.getWorld().playSound(mPlayer.getLocation(), Sound.BLOCK_GRAVEL_STEP, SoundCategory.PLAYERS, 0.8f, 1.2f);
            mPlayer.getWorld().playSound(mPlayer.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.PLAYERS, 1.3f, 1.5f);
        }
        else{
            new ArcParticleSet()
                    .setParticle(Particle.END_ROD)
                    .setArcWidth(20)
                    .setArcLength(72)
                    .setDuration(ticksPerSlash*2)
                    .setArcAngle(720)
                    .setCenter(mPlayer.getEyeLocation().subtract(0, 0.2, 0).subtract(side.normalize().multiply(-0.9)))
                    .setAxis(v)
                    .setInnerRadius(3)
                    .setOuterRadius(spinAttackRange)
                    .generateParticles();
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), () -> {slashDamage(empowered, true);}, ticksPerSlash / 2);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), () -> {slashDamage(empowered, true);}, (ticksPerSlash * 3)/ 2);

            mPlayer.getWorld().playSound(mPlayer.getLocation(), Sound.BLOCK_GRAVEL_STEP, SoundCategory.PLAYERS, 0.8f, 1.2f);
            mPlayer.getWorld().playSound(mPlayer.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, SoundCategory.PLAYERS, 1.3f, 1.5f);
        }
    }

    private void slashDamage(boolean isEmpowered, boolean isFinal){
        Collection<Entity> entities;
        if(!isFinal){
            entities = EntityUtils.getEntitiesInCone(mPlayer.getEyeLocation(), attackRange, attackAngle);
        }

        else{
            entities = EntityUtils.getEntitiesInSphere(mPlayer.getEyeLocation(), spinAttackRange);
        }
        boolean grantCharge = false;

        for(Entity e : entities){
            if(e instanceof LivingEntity le && !(e instanceof Player)){
                DamageUtils.damage(mPlayer, le, new DamageEvent.Metadata(DamageEvent.DamageType.MAGIC_SKILL, DATA.getName()), attackDamage, true, false, false);
                grantCharge = true;
            }
        }
        if(isEmpowered){
            grantCharge = false;
        }
        if(grantCharge){
            mStaticCharge.incrementCharge();
        }

    }
}
