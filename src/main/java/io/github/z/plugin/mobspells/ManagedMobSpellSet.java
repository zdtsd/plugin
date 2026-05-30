package io.github.z.plugin.mobspells;

import java.util.ArrayList;
import java.util.List;

public abstract class ManagedMobSpellSet extends MobSpellSet {

    protected final List<CooldownMobSpell> mCooldownSpells = new ArrayList<>();

    @Override
    public void addSpell(MobSpell spell) {
        super.addSpell(spell);
        if (spell instanceof CooldownMobSpell c) {
            mCooldownSpells.add(c);
        }
    }

    @Override
    public void tick() {
        for (CooldownMobSpell spell : mCooldownSpells) {
            spell.cooldown(5);
        }
    }
}
