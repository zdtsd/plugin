package io.github.z.plugin.abilities.testing;

import io.github.z.plugin.abilities.Ability;
import io.github.z.plugin.abilities.AbilityData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TestingAbilityOne extends Ability {
    public static AbilityData<TestingAbilityOne> DATA = new AbilityData<>(TestingAbilityOne.class, "testing_ability", TestingAbilityOne::new)
            .scoreboardID("test1")
            .displayMaterial(Material.BARRIER);

    public TestingAbilityOne(Player player) {

    }

    @Override
    public AbilityData getData() {
        return DATA;
    }
}
