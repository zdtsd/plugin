package io.github.z.plugin.effects;

import io.github.z.plugin.itemstats.ItemStat;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EffectManager {
    private class EffectSet{
        //mEffects stored lists of effects under string keys that indicate the source of the ability. In each namespace, only the highest
        //level of each class of effect is considered.
        private final Map<String, List<Effect>> mEffects = new HashMap<>();
        private final Entity mEntity;

        public EffectSet(Entity entity){
            mEntity = entity;
        }

        public void addEffect(String originName, Effect effect){
            List<Effect> currentEffects = getOrCreateNamespace(originName);
            currentEffects.add(effect);
            mEffects.put(originName, currentEffects);
        }

        public void addEffects(String originName, List<Effect> effects){
            List<Effect> currentEffects = getOrCreateNamespace(originName);
            currentEffects.addAll(effects);
            mEffects.put(originName, currentEffects);
        }

        public void removeEffectsByName(String originName){
            mEffects.remove(originName);
        }

        private void sortNamespace(){

        }

        private List<Effect> getOrCreateNamespace(String name){
            return mEffects.containsKey(name) ? mEffects.get(name) : new ArrayList<>();
        }
    }
}
