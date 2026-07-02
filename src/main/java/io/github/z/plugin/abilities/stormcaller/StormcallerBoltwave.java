package io.github.z.plugin.abilities.stormcaller;

import io.github.z.plugin.Plugin;
import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.abilities.CooldownAbility;
import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.effects.effects.StunEffect;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.particles.PointParticleSet;
import io.github.z.plugin.utils.DamageUtils;
import io.github.z.plugin.utils.EntityUtils;
import io.github.z.plugin.utils.LocationUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.Collection;

public class StormcallerBoltwave extends CooldownAbility {

    public static AbilityData<StormcallerBoltwave> DATA = new AbilityData<>(StormcallerBoltwave.class, "Boltwave", StormcallerBoltwave::new)
            .scoreboardID("StrmBolt")
            .displayMaterial(Material.LIGHTNING_ROD)
            .cooldown(3 * 20);

    private static final double CONE_RANGE = 6;
    private static final double CONE_DEGREES = Math.toRadians(180);
    private static final double IMPACT_DAMAGE = 8;
    private static final double KNOCKBACK = 1.5;
    private static final double IMPACT_FALL_DISTANCE = 1.0;
    private static final int STUN_DURATION = 1*20, EMPOWERED_STUN_DURATION = 3 * 20, EMPOWERED_KILL_REFRESH = 2*20;
    private boolean mIsEmpowered = false;



    public StormcallerBoltwave(Player player) {
        super(player);
    }

    @Override
    public AbilityData<?> getData() {
        return DATA;
    }

    private StormcallerStaticCharge mStaticCharge;

    private StormcallerStaticCharge getOrFetchStaticCharge() {
        if (mStaticCharge == null) {
            mStaticCharge = (StormcallerStaticCharge) AbilityManager.getAbility(mPlayer, StormcallerStaticCharge.class);
        }
        return mStaticCharge;
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (!mPlayer.isSneaking()) return;
        if (!isCastable()) return;

        Collection<Entity> entities = EntityUtils.getEntitiesInCone(mPlayer.getEyeLocation(), CONE_RANGE, CONE_DEGREES);
        entities.removeIf(e -> !(e instanceof LivingEntity) || e instanceof Player);
        if (entities.isEmpty()) return;

        spendCharge();
        mIsEmpowered = getOrFetchStaticCharge().onCast();

        mPlayer.getWorld().playSound(mPlayer.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, SoundCategory.PLAYERS, 1.0f, 1.0f);
        mPlayer.getWorld().playSound(mPlayer.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.PLAYERS, 1.0f, 1.0f);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity le) {
                le.knockback(KNOCKBACK,
                        mPlayer.getLocation().getX() - le.getLocation().getX(),
                        mPlayer.getLocation().getZ() - le.getLocation().getZ());
                Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), () -> {applyImpact(le);}, 1);
            }
        }
    }

    private void applyImpact(LivingEntity le){
        Vector kbDirection = le.getLocation().toVector().subtract(mPlayer.getLocation().toVector());

        new BukkitRunnable() {
            double mFallDistanceLastTick = EntityUtils.getFallDistance(le);
            @Override
            public void run() {
                if (checkForImpact(mFallDistanceLastTick, kbDirection, le)) {
                    onImpact(le);
                    this.cancel();
                }
                else if(le.isOnGround() || !le.isValid()){
                    this.cancel();
                }
                mFallDistanceLastTick =  EntityUtils.getFallDistance(le);
            }
        }.runTaskTimer(Plugin.getPlugin(), 1, 2);
    }

    private boolean checkForImpact(double fallDistanceLastTick, Vector kbDirection, LivingEntity le) {
        if (fallDistanceLastTick > IMPACT_FALL_DISTANCE && le.isOnGround()) {
            return true;
        }

        BoundingBox offsetBox = le.getBoundingBox().clone();
        offsetBox.shift(kbDirection.getX(), 0.0, kbDirection.getZ());
        offsetBox.expand(0.1, -0.3, 0.1);

        if (LocationUtils.collidesWithBlocks(offsetBox, le.getWorld(), false)) {
            return true;
        }

        return false;
    }
    private void onImpact(LivingEntity le){
        new PointParticleSet()
                .setParticle(Particle.EXPLOSION)
                .setLocation(le.getEyeLocation())
                .generateParticles();
        DamageUtils.damage(mPlayer, le,
                new DamageEvent.Metadata(DamageEvent.DamageType.MAGIC_SKILL, DATA.getName()),
                IMPACT_DAMAGE, true, false, true);
        if(mIsEmpowered && le.getHealth() <= 0){
            killRefresh();
        }
        EffectManager.applyEffect(le, mPlayer, new StunEffect(mIsEmpowered ? EMPOWERED_STUN_DURATION : STUN_DURATION, 1), DATA.getName());
        //TODO: Change this to not be stolen from MM
        le.getWorld().playSound(le.getLocation(), Sound.ENTITY_BLAZE_HURT, SoundCategory.PLAYERS, 1.5f, 1.35f);
        le.getWorld().playSound(le.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, SoundCategory.PLAYERS, 1.5f, 0.5f);
        le.getWorld().playSound(le.getLocation(), Sound.BLOCK_POINTED_DRIPSTONE_LAND, SoundCategory.PLAYERS, 1.7f, 0.5f);
        le.getWorld().playSound(le.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1.7f, 0.5f);
    }

    private void killRefresh(){
        progressCooldown(EMPOWERED_KILL_REFRESH);
    }
}
