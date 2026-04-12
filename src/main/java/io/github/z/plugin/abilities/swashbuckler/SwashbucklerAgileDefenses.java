package io.github.z.plugin.abilities.swashbuckler;

import io.github.z.plugin.abilities.Ability;
import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.effects.StunEffect;
import io.github.z.plugin.events.DamageEvent;
import org.bukkit.entity.Entity;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SwashbucklerAgileDefenses extends Ability {

    public static AbilityData<SwashbucklerAgileDefenses> DATA = new AbilityData<>(SwashbucklerAgileDefenses.class, "Agile Defenses", SwashbucklerAgileDefenses::new)
            .scoreboardID("SwshAgileDef")
            .displayMaterial(Material.FEATHER);

    public SwashbucklerAgileDefenses(Player mPlayer) {
        super(mPlayer);
    }

    @Override
    public AbilityData<?> getData() {
        return DATA;
    }

    private SwashbucklerStance playerStance;
    private SwashbucklerStance getOrFetchStance() {
        if (playerStance == null) {
            playerStance = (SwashbucklerStance) AbilityManager.getAbility(mPlayer, SwashbucklerStance.class);
        }
        return playerStance;
    }

    private boolean mAvailable = true;
    private SwashbucklerStance.SwashbucklerStanceType mSpentInStance = null;

    @Override
    public List<String> getSidebarLines() {
        List<String> lines = new ArrayList<>();
        lines.add((mAvailable ? "&b&l" : "&4&l") + DATA.getName());
        return lines;
    }

    @Override
    public void tick(Player player, boolean twoHz, boolean oneHz) {
        if (!mAvailable && getOrFetchStance().getStance() != mSpentInStance) {
            mAvailable = true;
        }
    }

    @Override
    public void onHurt(DamageEvent event) {
        if (!mAvailable) return;

        SwashbucklerStance.SwashbucklerStanceType currentStance = getOrFetchStance().getStance();

        boolean triggered =
                (currentStance == SwashbucklerStance.SwashbucklerStanceType.DEFENSIVE && event.isProjectile()) ||
                (currentStance == SwashbucklerStance.SwashbucklerStanceType.OFFENSIVE && event.isMelee());

        if (triggered) {
            event.setCancelled(true);
            mAvailable = false;
            mSpentInStance = currentStance;
            Entity source = event.getSource();
            if (source != null) {
                EffectManager.applyEffect(source, mPlayer, new StunEffect(20, 1), StunEffect.stunNamespace);
            }
            if (event.isMelee()) {
                mPlayer.getWorld().playSound(mPlayer.getLocation(), Sound.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 1.0f, 1.0f);
            } else {
                mPlayer.getWorld().playSound(mPlayer.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
        }
    }
}
