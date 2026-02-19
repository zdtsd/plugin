package io.github.z.plugin.abilities;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AbilitySet {
    private List<Ability> mAbilities = new ArrayList<>();
    public void addAbility(Ability ab){
        mAbilities.add(ab);
    }

    public List<Ability> getAbilities(){
        return mAbilities;
    }
}
