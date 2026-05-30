# CooldownMobSpell Implementation Plan

## Overview of Changes

| File | Action |
|------|--------|
| `MobSpellData.java` | Add cooldown/channel/bossbar fields with defaults |
| `MobSpellSet.java` | Add `tick()` no-op hook |
| `MobSpellManager.java` | Call `spellSet.tick()`; create `StandardManagedMobSpellSet` when CooldownMobSpells are present |
| `CooldownMobSpell.java` | New abstract class |
| `ManagedMobSpellSet.java` | New abstract class |
| `StandardManagedMobSpellSet.java` | New concrete class |

---

## Step 1 — `MobSpellData.java`

Add five new fields with default values so existing plain `MobSpell` subclasses are unaffected:

```java
private int    mCooldown         = 0;
private int    mChannelDuration  = 0;
private boolean mShowBossBar     = false;
private BossBar.Color mBossBarColor = BossBar.Color.BLUE;
private boolean mIsCancellable   = false;
```

Add builder-style setters (return `this`) matching the existing `priority()` pattern:

```java
public MobSpellData<T> cooldown(int ticks)              { mCooldown = ticks; return this; }
public MobSpellData<T> channelDuration(int ticks)       { mChannelDuration = ticks; return this; }
public MobSpellData<T> showBossBar(BossBar.Color color) { mShowBossBar = true; mBossBarColor = color; return this; }
public MobSpellData<T> cancellable()                    { mIsCancellable = true; return this; }
```

Add plain getters for all five fields.

---

## Step 2 — `MobSpellSet.java`

Add a `tick()` method with an empty default body. `MobSpellManager` will call this on every 4 Hz tick, and `ManagedMobSpellSet` will override it.

```java
public void tick() {}
```

---

## Step 3 — `CooldownMobSpell.java`

New abstract class in `mobspells/`, extending `MobSpell`.

### Fields

```java
private int     mCurrentCooldown = 0;
private boolean mIsChanneling    = false;
private BossBar mActiveBossBar   = null;
private BukkitTask mChannelTask  = null;
```

### `cooldown(int ticks)`

```java
public void cooldown(int ticks) {
    mCurrentCooldown = Math.max(0, mCurrentCooldown - ticks);
}
```

### `startChannel()`

```java
public void startChannel() {
    if (mIsChanneling) return;
    mIsChanneling = true;

    int duration = getData().getChannelDuration();

    if (duration <= 0) {
        finishCast();
        return;
    }

    if (getData().getShowBossBar()) {
        mActiveBossBar = Bukkit.createBossBar(
            getData().getName(), getData().getBossBarColor(), BarStyle.SOLID);
        mActiveBossBar.setProgress(1.0);
        Bukkit.getOnlinePlayers().forEach(mActiveBossBar::addPlayer);
    }

    int[] remaining = { duration };
    mChannelTask = Bukkit.getScheduler().runTaskTimer(Plugin.getPlugin(), () -> {
        remaining[0]--;
        onChannel();
        if (mActiveBossBar != null) {
            mActiveBossBar.setProgress((double) remaining[0] / duration);
        }
        if (remaining[0] <= 0) {
            finishCast();
        }
    }, 1L, 1L);
}
```

### `finishCast()` — private helper

```java
private void finishCast() {
    if (mChannelTask != null) {
        mChannelTask.cancel();
        mChannelTask = null;
    }
    if (mActiveBossBar != null) {
        mActiveBossBar.removeAll();
        mActiveBossBar = null;
    }
    castSpell();
    mIsChanneling = false;
    mCurrentCooldown = getData().getCooldown();
}
```

### Other members

```java
public void    onChannel()    {}           // public, overridable, default no-op
public boolean isChanneling() { return mIsChanneling; }
public int     getCurrentCooldown() { return mCurrentCooldown; }

protected abstract void castSpell();       // private in spec; use protected so subclasses in same package can implement
```

> **Note on `castSpell` visibility:** The spec says private abstract, which is not legal in Java. Use `protected abstract` instead.

---

## Step 4 — `ManagedMobSpellSet.java`

New abstract class in `mobspells/`, extending `MobSpellSet`.

```java
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
```

---

## Step 5 — `StandardManagedMobSpellSet.java`

New concrete class in `mobspells/`, extending `ManagedMobSpellSet`.

```java
public class StandardManagedMobSpellSet extends ManagedMobSpellSet {

    @Override
    public void tick() {
        super.tick();   // runs cooldown(5) on all CooldownMobSpells
        for (CooldownMobSpell spell : mCooldownSpells) {
            if (!spell.isChanneling() && spell.getCurrentCooldown() <= 0) {
                spell.startChannel();
            }
        }
    }
}
```

---

## Step 6 — `MobSpellManager.java`

### Call `spellSet.tick()` in the main tick loop

Inside `tick(boolean twoHz, boolean oneHz)`, after cleaning up dead entities and before (or after) routing individual spell ticks, call `tick()` on each active `MobSpellSet`:

```java
for (MobSpellSet set : mSpells.values()) {
    set.tick();
}
```

### Use `StandardManagedMobSpellSet` when CooldownMobSpells are present

In `grantSpells(LivingEntity entity)`, after all spells have been instantiated and before storing the set, check whether any spell is a `CooldownMobSpell`. If so, use `StandardManagedMobSpellSet`; otherwise use plain `MobSpellSet`:

```java
boolean hasCooldownSpell = spells.stream().anyMatch(s -> s instanceof CooldownMobSpell);
MobSpellSet set = hasCooldownSpell ? new StandardManagedMobSpellSet() : new MobSpellSet();
for (MobSpell spell : spells) {
    set.addSpell(spell);
}
```

---

## Data Flow Summary

```
Timer (1 tick)
  └─ MobSpellManager.tick()
       ├─ Per entity: spell.tick()          ← existing passive hooks unchanged
       └─ Per MobSpellSet: spellSet.tick()
            └─ StandardManagedMobSpellSet.tick()
                 ├─ CooldownMobSpell.cooldown(5)     ← counts down cooldown
                 └─ if cooldown <= 0 && !channeling
                       └─ spell.startChannel()
                            ├─ Shows BossBar to all players (if showBossBar)
                            ├─ Schedules per-tick Bukkit task
                            │    ├─ onChannel()           ← overridable hook
                            │    └─ Updates BossBar progress
                            └─ When done: castSpell() + reset cooldown
```
