package io.github.z.plugin.abilities;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AbilitySet {
    private List<Ability> mAbilities = new ArrayList<>();




    public void addAbility(Ability ab){
        mAbilities.add(ab);
    }

    public void tick(Player player, boolean twoHz, boolean oneHz) {
        for(Ability ability : mAbilities){
            ability.tick(player, twoHz, oneHz);
        }
    }
}
