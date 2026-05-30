package io.github.z.plugin.mobspells.active;

import io.github.z.plugin.mobspells.CooldownMobSpell;
import io.github.z.plugin.mobspells.MobSpellData;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.LivingEntity;

public class KillSpell extends CooldownMobSpell {
    public static final MobSpellData<KillSpell> DATA = new MobSpellData<>(
            KillSpell.class, "KillSpell", KillSpell::new)
            .cooldown(100)
            .channelDuration(40)
            .showBossBar(BarColor.RED);

    public KillSpell(LivingEntity entity) {
        super(entity);
    }

    @Override
    public MobSpellData<?> getData() {
        return DATA;
    }

    @Override
    protected void castSpell() {
        getEntity().setHealth(0);
    }
}
