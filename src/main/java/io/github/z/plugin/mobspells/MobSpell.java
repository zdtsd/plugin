package io.github.z.plugin.mobspells;

import io.github.z.plugin.events.ApplyEffectEvent;
import io.github.z.plugin.events.DamageEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityExplodeEvent;

public abstract class MobSpell {
    private int mLevel = 0;
    protected final LivingEntity mEntity;

    public MobSpell(LivingEntity entity) {
        this.mEntity = entity;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

    public int getLevel() {
        return mLevel;
    }

    public LivingEntity getEntity() {
        return mEntity;
    }

    public abstract MobSpellData<?> getData();

    public void tick(LivingEntity entity, boolean twoHz, boolean oneHz) {}
    public void onDamage(DamageEvent event) {}
    public void onHurt(DamageEvent event) {}
    public void onApplyEffect(ApplyEffectEvent event) {}
    public void onReceiveEffect(ApplyEffectEvent event) {}
    public void onCreeperExplode(EntityExplodeEvent event) {}
}
