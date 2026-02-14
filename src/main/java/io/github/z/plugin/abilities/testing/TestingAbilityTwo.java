package io.github.z.plugin.abilities.testing;

import io.github.z.plugin.abilities.Ability;
import io.github.z.plugin.abilities.AbilityData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TestingAbilityTwo extends Ability {
    public static AbilityData<TestingAbilityTwo> DATA = new AbilityData<>(TestingAbilityTwo.class, "testing_ability_2", TestingAbilityTwo::new)
            .scoreboardID("test2")
            .displayMaterial(Material.BARRIER);

    public TestingAbilityTwo(Player player) {

    }
    @Override
    public AbilityData getData() {
        return DATA;
    }

    @Override
    public void tick(Player player, boolean twoHz, boolean oneHz) {
        Bukkit.getLogger().info("Ability 2 active!");
    }
}
