package io.github.z.plugin.abilities.stormcaller;

import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.abilities.CooldownAbility;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.particles.LineParticleSet;
import io.github.z.plugin.utils.DamageUtils;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class StormcallerZap extends CooldownAbility {

    public static AbilityData<StormcallerZap> DATA = new AbilityData<>(StormcallerZap.class, "Zap", StormcallerZap::new)
            .scoreboardID("StrmZap")
            .displayMaterial(Material.LIGHTNING_ROD)
            .cooldown(5 * 20)
            .maxCharges(3);

    private static final double NORMAL_RANGE = 8;
    private static final double EMPOWERED_RANGE = 24;
    private static final double NORMAL_DAMAGE = 5;
    private static final double EMPOWERED_DAMAGE = 25;
    private static final double NORMAL_KNOCKBACK = 0.1;
    private static final double EMPOWERED_KNOCKBACK = 0.2;
    private static final int NORMAL_PARTICLES = 20;
    private static final int EMPOWERED_PARTICLES = 60;

    private int mCastLockout = 0;
    private StormcallerStaticCharge mStaticCharge;

    private StormcallerStaticCharge getOrFetchStaticCharge() {
        if (mStaticCharge == null) {
            mStaticCharge = (StormcallerStaticCharge) AbilityManager.getAbility(mPlayer, StormcallerStaticCharge.class);
        }
        return mStaticCharge;
    }

    public StormcallerZap(Player player) {
        super(player);
    }

    @Override
    public AbilityData<?> getData() {
        return DATA;
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (mPlayer.isSneaking()) return;
        if (mCastLockout > 0) return;
        if (!isCastable()) return;

        boolean empowered = getOrFetchStaticCharge().onCast();

        double range = empowered ? EMPOWERED_RANGE : NORMAL_RANGE;
        double damage = empowered ? EMPOWERED_DAMAGE : NORMAL_DAMAGE;
        double knockback = empowered ? EMPOWERED_KNOCKBACK : NORMAL_KNOCKBACK;
        int maxParticles = empowered ? EMPOWERED_PARTICLES : NORMAL_PARTICLES;
        Sound sound = empowered ? Sound.ENTITY_FIREWORK_ROCKET_BLAST : Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR;

        spendCharge();
        mCastLockout = 1;

        Location eye = mPlayer.getEyeLocation();
        Vector direction = eye.getDirection();

        RayTraceResult rayResult = mPlayer.getWorld().rayTraceEntities(
                eye, direction, range, 0.25,
                entity -> entity instanceof LivingEntity && !(entity instanceof Player)
        );

        LivingEntity target = null;
        Location hitLocation;

        if (rayResult != null && rayResult.getHitEntity() instanceof LivingEntity le) {
            target = le;
            hitLocation = rayResult.getHitPosition().toLocation(mPlayer.getWorld());
        } else {
            hitLocation = eye.clone().add(direction.clone().multiply(range));
        }

        drawBeam(eye, hitLocation, maxParticles);
        mPlayer.getWorld().playSound(mPlayer.getLocation(), sound, SoundCategory.PLAYERS, 1.0f, 1.0f);

        if (target != null) {
            DamageUtils.damage(mPlayer, target,
                    new DamageEvent.Metadata(DamageEvent.DamageType.MAGIC_SKILL, DATA.getName()),
                    damage, true, false, true);
            target.knockback(knockback,
                    mPlayer.getLocation().getX() - target.getLocation().getX(),
                    mPlayer.getLocation().getZ() - target.getLocation().getZ());
        }
    }

    @Override
    public void tick(Player player, boolean twoHz, boolean oneHz) {
        if (mCastLockout > 0) mCastLockout--;
    }

    private void drawBeam(Location start, Location end, int maxParticles) {
        new LineParticleSet()
                .setStart(start)
                .setEnd(end)
                .setParticle(Particle.WAX_ON)
                .setCount(maxParticles)
                .generateParticles();
    }
}
