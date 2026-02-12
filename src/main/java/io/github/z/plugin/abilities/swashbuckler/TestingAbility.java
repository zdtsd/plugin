package io.github.z.plugin.abilities.swashbuckler;

import io.github.z.plugin.abilities.Ability;
import io.github.z.plugin.abilities.AbilityData;
import org.bukkit.entity.Player;

public class TestingAbility extends Ability {
    public static AbilityData<TestingAbility> DATA = new AbilityData<TestingAbility>(TestingAbility.class, "testing_ability", TestingAbility::new);

    public TestingAbility(Player player) {

    }
}
