package io.github.z.plugin.mobspells;

import org.bukkit.entity.LivingEntity;

import java.util.function.Function;

// Each MobSpell class should have a static MobSpellData field which records information about the class.
public class MobSpellData<T extends MobSpell> {
    private final Class<T> mSpell;
    private final Function<LivingEntity, T> mConstructor;
    private final String mName;
    private double mPriorityAmount = 1000;

    public MobSpellData(Class<T> spellClass, String name, Function<LivingEntity, T> constructor) {
        mSpell = spellClass;
        mName = name;
        mConstructor = constructor;
    }

    // Builder functions
    public MobSpellData<T> priority(double prio) {
        mPriorityAmount = prio;
        return this;
    }


    // Getters
    public MobSpell getNewInstance(LivingEntity entity) {
        return mConstructor.apply(entity);
    }

    public String getName() {
        return mName;
    }

    public double getPriorityAmount() {
        return mPriorityAmount;
    }
}
