package io.github.z.plugin.effects;

import io.github.z.plugin.GenericEntityModifier;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.ItemStat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class EffectManager {
    private class EffectSet{
        //mEffects stored lists of effects under string keys that indicate the source of the ability. In each namespace, only the highest
        //level of each class of effect is considered.
        private final Map<String, List<Effect>> mEffects = new HashMap<>();
        private List<Effect> mActiveEffects = new ArrayList<>();
        private final Entity mEntity;

        public EffectSet(Entity entity){
            mEntity = entity;
        }

        public void addEffect(String originName, Effect effect){
            List<Effect> currentEffects = getOrCreateNamespace(originName);
            currentEffects.add(effect);
            currentEffects.sort(Effect::compareTo);
            mEffects.put(originName, currentEffects);
            updateActiveEffectsList();
        }

        public void removeEffectsByName(String originName){
            mEffects.remove(originName);
        }

        private void updateActiveEffectsList(){
            mActiveEffects = new ArrayList<>();
            for(List<Effect> effects : mEffects.values()){
                if(!effects.isEmpty()){
                    mActiveEffects.add(effects.getLast());
                }
            }
            mActiveEffects.sort(Comparator.comparingDouble(GenericEntityModifier::getPriorityAmount));
        }

        public Effect getActiveEffect(String name){
            return mEffects.get(name) != null ? mEffects.get(name).getLast() : null;
        }

        public List<Effect> getActiveEffects(){
            return mActiveEffects;
        }

        private void sortNamespace(){

        }

        private List<Effect> getOrCreateNamespace(String name){
            return mEffects.containsKey(name) ? mEffects.get(name) : new ArrayList<>();
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

            boolean removedActiveEffect = false;
            //Handle duration ticks.
            for(String s : mEffects.keySet()){
                List<Effect> effects = mEffects.get(s);
                for(Effect effect : effects){
                    effect.reduceDuration(5);
                }
                while(!effects.isEmpty() && effects.getLast().getDuration() <= 0){
                    effects.removeLast();
                    removedActiveEffect = true;
                }
                if(effects.isEmpty()){
                    mEffects.remove(s);
                }
            }
            if(removedActiveEffect){
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
        EffectSet set = mInstance.getOrCreateEffectSet(entity);
        set.addEffect(originName, effect);
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

    public static void onDamage(Entity entity, DamageEvent event){
        EffectSet set = mInstance.getOrCreateEffectSet(entity);
        for(Effect effect : set.getActiveEffects()){
            effect.onDamage(entity, event, effect.getStrength());
        }
    }
}
