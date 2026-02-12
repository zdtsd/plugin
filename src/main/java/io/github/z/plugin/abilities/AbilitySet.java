package io.github.z.plugin.abilities;

import java.util.ArrayList;
import java.util.List;

public class AbilitySet {
    private List<Ability> mAbilities = new ArrayList<>();




    public void addAbility(Ability ab){
        mAbilities.add(ab);
    }
}
