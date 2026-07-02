package io.github.z.plugin.abilities.testing;

import io.github.z.plugin.abilities.Ability;
import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.particles.ArcParticleSet;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.util.Vector;

public class TestingAbilityArcParticle extends Ability {
    public static AbilityData<TestingAbilityArcParticle> DATA = new AbilityData<>(TestingAbilityArcParticle.class, "testing_ability_arc", TestingAbilityArcParticle::new)
            .scoreboardID("test_arc")
            .displayMaterial(Material.END_ROD);

    public TestingAbilityArcParticle(Player player) {
        super(player);
    }

    @Override
    public AbilityData getData() {
        return DATA;
    }


    @Override
    public void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        Vector side = mPlayer.getEyeLocation().getDirection();
        side.setY(0);
        side.rotateAroundY(Math.toRadians(90));
        Vector v = mPlayer.getEyeLocation().getDirection();
        v.rotateAroundNonUnitAxis(side, Math.toRadians(90));
        v.rotateAroundNonUnitAxis(mPlayer.getEyeLocation().getDirection(), Math.toRadians(45));
        new ArcParticleSet()
                .setParticle(Particle.END_ROD)
                .setArcWidth(10)
                .setArcLength(18)
                .setDuration(6)
                .setArcAngle(120)
                .setCenter(mPlayer.getEyeLocation().subtract(0, 0.2, 0).subtract(side.normalize().multiply(-0.9)))
                .setAxis(v)
                .setInnerRadius(3)
                .setOuterRadius(6)
                .generateParticles();
    }

    @Override
    public void onDropKey() {
        Vector v = mPlayer.getEyeLocation().getDirection();
        v.setY(0);
        v.rotateAroundY(Math.toRadians(90));
        new ArcParticleSet()
                .setParticle(Particle.END_ROD)
                .setArcWidth(10)
                .setArcLength(144)
                .setDuration(40)
                .setArcAngle(720)
                .setCenter(mPlayer.getEyeLocation())
                .setAxis(v)
                .setInnerRadius(2)
                .setOuterRadius(5)
                .generateParticles();
    }

    @Override
    public void tick(Player player, boolean twoHz, boolean oneHz) {
    }
}
