package io.github.z.plugin.mobspells;

import io.github.z.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

public abstract class CooldownMobSpell extends MobSpell {

    private int        mCurrentCooldown = 0;
    private boolean    mIsChanneling    = false;
    private BossBar    mActiveBossBar   = null;
    private BukkitTask mChannelTask     = null;

    public CooldownMobSpell(LivingEntity entity) {
        super(entity);
    }

    public void cooldown(int ticks) {
        mCurrentCooldown = Math.max(0, mCurrentCooldown - ticks);
    }

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

    public void onChannel() {}

    public boolean isChanneling()      { return mIsChanneling; }
    public int     getCurrentCooldown() { return mCurrentCooldown; }

    protected abstract void castSpell();
}
