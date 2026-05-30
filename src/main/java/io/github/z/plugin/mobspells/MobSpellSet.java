package io.github.z.plugin.mobspells;

import java.util.ArrayList;
import java.util.List;

public class MobSpellSet {
    private List<MobSpell> mSpells = new ArrayList<>();

    public void addSpell(MobSpell spell) {
        mSpells.add(spell);
    }

    public List<MobSpell> getSpells() {
        return mSpells;
    }

    public void tick() {}
}
