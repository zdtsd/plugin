package io.github.z.plugin.abilities.testing;

import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.abilities.CooldownAbility;
import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.effects.effects.BaseMoveSpeedModifyEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class TestingAbilityWeakness extends CooldownAbility {

    public static AbilityData<TestingAbilityWeakness> DATA = new AbilityData<>(TestingAbilityWeakness.class, "testing_ability_weakness", TestingAbilityWeakness::new)
            .scoreboardID("testWeak")
            .displayMaterial(Material.POTION)
            .cooldown(10);

    public TestingAbilityWeakness(Player mPlayer) {
        super(mPlayer);
    }

    @Override
    public AbilityData<?> getData() {
        return DATA;
    }

    @Override
    public void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        BaseMoveSpeedModifyEffect effect = new BaseMoveSpeedModifyEffect(5 * 20, -30);
        EffectManager.applyEffect(mPlayer, effect, getData().getName());
    }
}
