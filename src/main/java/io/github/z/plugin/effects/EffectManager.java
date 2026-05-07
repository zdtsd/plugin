package io.github.z.plugin.effects;

import io.github.z.plugin.GenericEntityModifier;
import io.github.z.plugin.events.ApplyEffectEvent;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.events.PlayerLandsOnGroundEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EffectManager {

    private class EffectSet{
        //mEffects stored lists of effects under string keys that indicate the source of the ability. In each namespace, only the highest
        //level of each class of effect is considered.
        private final Map<String, PriorityQueue<Effect>> mEffects = new HashMap<>();
        private List<Effect> mActiveEffects = new ArrayList<>();
        private final Entity mEntity;


        public EffectSet(Entity entity){
            mEntity = entity;
        }

        public void addEffect(@NotNull String originName, @NotNull Effect effect){
            PriorityQueue<Effect> currentEffects = getOrCreateNamespace(originName);
            Effect oldEffect = currentEffects.peek();
            currentEffects.add(effect);
            mEffects.put(originName, currentEffects);
            updateActiveEffectsList();


            if(currentEffects.peek() == effect){
                effect.onEffectAdd(oldEffect, mEntity);
            }
        }

        public void removeEffectsByName(@NotNull String originName){
            getActiveEffect(originName).onEffectRemove(null, mEntity);
            mEffects.remove(originName);
            updateActiveEffectsList();
        }

        private void updateActiveEffectsList(){
            mActiveEffects = new ArrayList<>();
            for(PriorityQueue<Effect> effects : mEffects.values()){
                if(!effects.isEmpty()){
                    mActiveEffects.add(effects.peek());
                }
            }
            mActiveEffects.sort(Comparator.comparingDouble(GenericEntityModifier::getPriorityAmount));
        }

        //WARNING: This does not use the ActiveEffects list and therefore CAN be desynced.
        public Effect getActiveEffect(String originName){
            return mEffects.get(originName) != null ? mEffects.get(originName).peek(): null;
        }

        public List<Effect> getActiveEffects(){
            return mActiveEffects;
        }


        private PriorityQueue<Effect> getOrCreateNamespace(String originName){
            return mEffects.containsKey(originName) ? mEffects.get(originName) : new PriorityQueue<>(Effect::compareTo);
        }

        public List<String> getSidebarLines(){
            List<String> out = new ArrayList<>();
            for(String s : mEffects.keySet()){
                if(!mEffects.get(s).isEmpty()){
                    //Ignore warning
                    out.addAll(getActiveEffect(s).getSidebarLines());
                }
            }
            return out;
        }

        //4Hz
        public void tick(){
            //TODO: Handle tick effects.
            //Handle duration ticks.
            boolean updateActiveEffects = false;
            for(String s : mEffects.keySet()){
                PriorityQueue<Effect> effects = mEffects.get(s);
                Effect activeEffect = effects.peek();
                for(Effect effect : effects){
                    effect.reduceDuration(5);
                }
                effects.removeIf((effect) -> effect.getDuration() <= 0);
                if(effects.peek() != activeEffect){
                    updateActiveEffects = true;
                    activeEffect.onEffectRemove(effects.peek(), mEntity);
                    if(effects.peek() != null){
                        effects.peek().onEffectAdd(activeEffect, mEntity);
                    }
                }
                if(effects.isEmpty()){
                    mEffects.remove(s);
                }
            }
            if(updateActiveEffects){
                updateActiveEffectsList();
            }
        }

    }


    private static EffectManager mInstance;
    private final Map<UUID, EffectSet> mEffects = new WeakHashMap<>();


    public EffectManager(){
        mInstance = this;
    }

    public static EffectManager getEffectManager(){
        return mInstance;
    }

    public static void applyEffect(Entity entity, Effect effect, String originName){
        applyEffect(entity, null, effect, originName);
    }

    public static void applyEffect(Entity entity, Entity sourceEntity, Effect effect, String originName){
        ApplyEffectEvent event = new ApplyEffectEvent(entity, sourceEntity, effect, originName);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        EffectSet set = mInstance.getOrCreateEffectSet(event.getEntity());
        set.addEffect(event.getOriginName(), event.getEffect());
    }

    public List<String> getSidebarLines(Player player){
        return(getOrCreateEffectSet(player).getSidebarLines());
    }

    private EffectSet getOrCreateEffectSet(Entity entity){
        if(mEffects.containsKey(entity.getUniqueId())){
            return mEffects.get(entity.getUniqueId());
        }
        else{
            mEffects.put(entity.getUniqueId(), new EffectSet(entity));
            return mEffects.get(entity.getUniqueId());
        }
    }

    public void tick(){
        for(EffectSet set : mEffects.values()){
            set.tick();
        }
    }

    public static void onHurt(Entity entity, DamageEvent event){
        EffectSet set = mInstance.getOrCreateEffectSet(entity);
        for(Effect effect : set.getActiveEffects()){
            effect.onHurt(entity, event, effect.getStrength());
        }
    }

    public static void onApplyEffect(Entity entity, ApplyEffectEvent event){
        EffectSet set = mInstance.getOrCreateEffectSet(entity);
        for(Effect effect : set.getActiveEffects()){
            effect.onApplyEffect(entity, event, effect.getStrength());
        }
    }

    public static void onReceiveEffect(Entity entity, ApplyEffectEvent event){
        EffectSet set = mInstance.getOrCreateEffectSet(entity);
        for(Effect effect : set.getActiveEffects()){
            effect.onReceiveEffect(entity, event, effect.getStrength());
        }
    }

    public static void onPlayerLandsOnGround(Entity entity, PlayerLandsOnGroundEvent event){
        EffectSet set = mInstance.getOrCreateEffectSet(entity);
        for(Effect effect : set.getActiveEffects()){
            effect.onPlayerLandsOnGround(entity, event, effect.getStrength());
        }
    }

    public static void onDamage(Entity entity, DamageEvent event){
        EffectSet set = mInstance.getOrCreateEffectSet(entity);
        for(Effect effect : set.getActiveEffects()){
            effect.onDamage(entity, event, effect.getStrength());
        }
    }

    public static void onKill(Entity killer, DamageEvent event, LivingEntity damagee){
        EffectSet set = mInstance.getOrCreateEffectSet(killer);
        for(Effect effect : set.getActiveEffects()){
            effect.onKill(killer, event, damagee, effect.getStrength());
        }
    }

    public static void onDoubleJump(Player player, PlayerToggleFlightEvent event){
        EffectSet set = mInstance.getOrCreateEffectSet(player);
        for(Effect effect : set.getActiveEffects()){
            if(effect instanceof PlayerEffect playerEffect){
                playerEffect.onDoubleJump(player, event, playerEffect.getStrength());
            }
        }
    }
}
