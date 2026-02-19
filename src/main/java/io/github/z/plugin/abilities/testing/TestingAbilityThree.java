package io.github.z.plugin.abilities.testing;

import io.github.z.plugin.abilities.Ability;
import io.github.z.plugin.abilities.AbilityData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TestingAbilityThree extends Ability {
    public static AbilityData<TestingAbilityThree> DATA = new AbilityData<>(TestingAbilityThree.class, "testing_ability_3", TestingAbilityThree::new)
            .scoreboardID("test3")
            .displayMaterial(Material.BARRIER);

    public TestingAbilityThree(Player player) {
        super(player);
    }
    @Override
    public AbilityData getData() {
        return DATA;
    }

    @Override
    public void tick(Player player, boolean twoHz, boolean oneHz) {
        Bukkit.getLogger().info("Ability 3 active!");
    }
}
