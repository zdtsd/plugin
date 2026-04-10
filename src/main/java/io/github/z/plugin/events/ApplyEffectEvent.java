package io.github.z.plugin.events;

import io.github.z.plugin.effects.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ApplyEffectEvent extends Event implements Cancellable {

    private final Entity mEntity;
    private final @Nullable Entity mSourceEntity;
    private Effect mEffect;
    private String mOriginName;
    private boolean mCancelled = false;

    public ApplyEffectEvent(Entity entity, @Nullable Entity sourceEntity, Effect effect, String originName) {
        mEntity = entity;
        mSourceEntity = sourceEntity;
        mEffect = effect;
        mOriginName = originName;
    }

    public Entity getEntity() {
        return mEntity;
    }

    public @Nullable Entity getSourceEntity() {
        return mSourceEntity;
    }

    public Effect getEffect() {
        return mEffect;
    }

    public void setEffect(Effect effect) {
        mEffect = effect;
    }

    public String getOriginName() {
        return mOriginName;
    }

    public void setOriginName(String originName) {
        mOriginName = originName;
    }

    @Override
    public boolean isCancelled() {
        return mCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        mCancelled = cancelled;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
