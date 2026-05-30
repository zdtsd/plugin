package io.github.z.plugin.mobspells;

import org.bukkit.boss.BarColor;
import org.bukkit.entity.LivingEntity;

import java.util.function.Function;

public class MobSpellData<T extends MobSpell> {
    private final Class<T> mSpell;
    private final Function<LivingEntity, T> mConstructor;
    private final String mName;
    private double   mPriorityAmount   = 1000;
    private int      mCooldown         = 0;
    private int      mChannelDuration  = 0;
    private boolean  mShowBossBar      = false;
    private BarColor mBossBarColor     = BarColor.BLUE;
    private boolean  mIsCancellable    = false;

    public MobSpellData(Class<T> spellClass, String name, Function<LivingEntity, T> constructor) {
        mSpell = spellClass;
        mName = name;
        mConstructor = constructor;
    }

    // Builder methods
    public MobSpellData<T> priority(double prio)             { mPriorityAmount = prio;   return this; }
    public MobSpellData<T> cooldown(int ticks)               { mCooldown = ticks;         return this; }
    public MobSpellData<T> channelDuration(int ticks)        { mChannelDuration = ticks;  return this; }
    public MobSpellData<T> showBossBar(BarColor color)       { mShowBossBar = true; mBossBarColor = color; return this; }
    public MobSpellData<T> cancellable()                     { mIsCancellable = true;     return this; }

    // Getters
    public MobSpell  getNewInstance(LivingEntity entity) { return mConstructor.apply(entity); }
    public String    getName()            { return mName; }
    public double    getPriorityAmount()  { return mPriorityAmount; }
    public int       getCooldown()        { return mCooldown; }
    public int       getChannelDuration() { return mChannelDuration; }
    public boolean   getShowBossBar()     { return mShowBossBar; }
    public BarColor  getBossBarColor()    { return mBossBarColor; }
    public boolean   getIsCancellable()   { return mIsCancellable; }
}
