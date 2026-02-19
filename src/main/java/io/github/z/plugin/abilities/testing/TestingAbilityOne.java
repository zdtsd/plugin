package io.github.z.plugin.abilities.testing;

import io.github.z.plugin.abilities.Ability;
import io.github.z.plugin.abilities.AbilityData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TestingAbilityOne extends Ability {
    public static AbilityData<TestingAbilityOne> DATA = new AbilityData<>(TestingAbilityOne.class, "testing_ability", TestingAbilityOne::new)
            .scoreboardID("test1")
            .displayMaterial(Material.BARRIER);

    public TestingAbilityOne(Player player) {
        super(player);
    }

    @Override
    public AbilityData getData() {
        return DATA;
    }

    @Override
    public void tick(Player player, boolean twoHz, boolean oneHz) {
        Bukkit.getLogger().info("Ability 1 active!");
    }
}
