package io.github.z.plugin.abilities.swashbuckler;

import io.github.z.plugin.abilities.Ability;
import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.effects.StunEffect;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.events.PlayerLandsOnGroundEvent;
import io.github.z.plugin.particles.CircleParticleSet;
import io.github.z.plugin.utils.DamageUtils;
import io.github.z.plugin.utils.EntityUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class SwashbucklerMeteorSlam extends Ability {

    public static AbilityData<SwashbucklerMeteorSlam> DATA = new AbilityData<>(SwashbucklerMeteorSlam.class, "Meteor Slam", SwashbucklerMeteorSlam::new)
            .scoreboardID("SwshMeteorSlam")
            .displayMaterial(Material.IRON_BOOTS);

    private static final double mMinFallDistance = 5, mRadius = 5, mDamage = 10;
    private static final int mStunDuration = 20 * 2;
    private static final Particle mParticle = Particle.EXPLOSION;
    private static final int mParticleCount = 12;

    private SwashbucklerStance mPlayerStance;
    private SwashbucklerStance getOrFetchStance() {
        if (mPlayerStance == null) {
            mPlayerStance = (SwashbucklerStance) AbilityManager.getAbility(mPlayer, SwashbucklerStance.class);
        }
        return mPlayerStance;
    }

    public SwashbucklerMeteorSlam(Player player) {
        super(player);
    }

    @Override
    public AbilityData<?> getData() {
        return DATA;
    }

    @Override
    public void onPlayerLandsOnGround(PlayerLandsOnGroundEvent event) {
        if (mPlayer.getFallDistance() <= mMinFallDistance) {
            return;
        }

        boolean offensiveStance = getOrFetchStance().getStance() == SwashbucklerStance.SwashbucklerStanceType.OFFENSIVE;

        boolean hitAny = false;
        for (Entity entity : EntityUtils.getEntitiesInSphere(mPlayer.getLocation(), mRadius)) {
            if (entity instanceof LivingEntity le && !(entity instanceof Player)) {
                DamageUtils.damage(mPlayer, le, DamageEvent.DamageType.MELEE_SKILL, mDamage, DATA.getName(), true, false);
                if (offensiveStance) {
                    EffectManager.applyEffect(le, new StunEffect(mStunDuration, 1), StunEffect.stunNamespace);
                }
                hitAny = true;
            }
        }


        mPlayer.getWorld().playSound(mPlayer.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.8f, 1.4f);
        new CircleParticleSet()
                .setCenter(mPlayer.getLocation())
                .setRadius(mRadius)
                .setParticle(mParticle)
                .setCount(mParticleCount)
                .generateParticles();

    }
}
