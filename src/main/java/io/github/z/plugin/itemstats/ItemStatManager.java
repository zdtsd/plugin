package io.github.z.plugin.itemstats;

import io.github.z.plugin.events.ApplyEffectEvent;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.events.PlayerLandsOnGroundEvent;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ItemStatManager {

    public static Map<UUID, PlayerItemStats> playerStats = new WeakHashMap<>();


    public static void tick(Player player, boolean twoHz, boolean oneHz){
        for(Map.Entry<UUID, PlayerItemStats> entry : playerStats.entrySet()){
            entry.getValue().tick(player, twoHz, oneHz);
        }
    }

    public static void updateMainhandStats(Player player, ItemStack newMainhand){
        PlayerItemStats stats = playerStats.get(player.getUniqueId());
        if(stats == null){
            stats = new PlayerItemStats(player);
            playerStats.put(player.getUniqueId(), stats);
        }
        stats.updateMainhandStats(player, newMainhand);
    }

    public static void updateArmorStats(Player player){
        PlayerItemStats stats = playerStats.get(player.getUniqueId());
        if(stats == null){
            stats = new PlayerItemStats(player);
            playerStats.put(player.getUniqueId(), stats);
        }
        stats.updateArmorStats(player);
    }

    public static void updateStats(Player player){
        updateMainhandStats(player, player.getInventory().getItemInMainHand());
        updateArmorStats(player);
    }


    public static double getStat(Player player, ItemStat stat){
        return playerStats.get(player.getUniqueId()).getItemStats().get(stat);
    }

    public static double getAttribute(Player player, AttributeType type){
        return getStat(player, type.getStat());
    }


    public static void onDamage(Player player, DamageEvent event){
        if(event.isCancelled()){
            Bukkit.getLogger().info("Custom damage event CANCELLED.");
            return;
        }
        if(playerStats.containsKey(player.getUniqueId())){
            for(Map.Entry<ItemStat, Double> entry : playerStats.get(player.getUniqueId()).getItemStats()){
                entry.getKey().onDamage(player, event, entry.getValue());
            }
        }
    }

    public static void onHurt(Player player, DamageEvent event){
        if(event.isCancelled()){
            Bukkit.getLogger().info("Custom damage event CANCELLED.");
            return;
        }
        if(playerStats.containsKey(player.getUniqueId())){
            for(Map.Entry<ItemStat, Double> entry : playerStats.get(player.getUniqueId()).getItemStats()){
                entry.getKey().onHurt(player, event, entry.getValue());
            }
        }
    }

    public static void onApplyEffect(Player player, ApplyEffectEvent event){
        if(playerStats.containsKey(player.getUniqueId())){
            for(Map.Entry<ItemStat, Double> entry : playerStats.get(player.getUniqueId()).getItemStats()){
                entry.getKey().onApplyEffect(player, event, entry.getValue());
            }
        }
    }

    public static void onReceiveEffect(Player player, ApplyEffectEvent event){
        if(playerStats.containsKey(player.getUniqueId())){
            for(Map.Entry<ItemStat, Double> entry : playerStats.get(player.getUniqueId()).getItemStats()){
                entry.getKey().onReceiveEffect(player, event, entry.getValue());
            }
        }
    }

    public static void onProjectileLaunch(Player player, ProjectileLaunchEvent event){
        if(playerStats.containsKey(player.getUniqueId())){
            for(Map.Entry<ItemStat, Double> entry : playerStats.get(player.getUniqueId()).getItemStats()){
                entry.getKey().onProjectileLaunch(player, event, entry.getValue());
            }
        }
    }

    public static void onBowShoot(Player player, EntityShootBowEvent event){
        if(playerStats.containsKey(player.getUniqueId())){
            for(Map.Entry<ItemStat, Double> entry : playerStats.get(player.getUniqueId()).getItemStats()){
                entry.getKey().onBowShoot(player, event, entry.getValue());
            }
        }
    }

    public static void onCrossbowShoot(Player player, EntityShootBowEvent event){
        if(playerStats.containsKey(player.getUniqueId())){
            for(Map.Entry<ItemStat, Double> entry : playerStats.get(player.getUniqueId()).getItemStats()){
                entry.getKey().onCrossbowShoot(player, event, entry.getValue());
            }
        }
    }

    public static void onCrossbowLoad(Player player, EntityLoadCrossbowEvent event){
        if(playerStats.containsKey(player.getUniqueId())){
            for(Map.Entry<ItemStat, Double> entry : playerStats.get(player.getUniqueId()).getItemStats()){
                entry.getKey().onCrossbowLoad(player, event, entry.getValue());
            }
        }
    }
    public static void onDoubleJump(Player player, PlayerToggleFlightEvent event){
        if(playerStats.containsKey(player.getUniqueId())){
            for(Map.Entry<ItemStat, Double> entry : playerStats.get(player.getUniqueId()).getItemStats()){
                entry.getKey().onDoubleJump(player, event, entry.getValue());
            }
        }
    }

    public static void onPlayerLandsOnGround(Player player, PlayerLandsOnGroundEvent event){
        if(playerStats.containsKey(player.getUniqueId())){
            for(Map.Entry<ItemStat, Double> entry : playerStats.get(player.getUniqueId()).getItemStats()){
                entry.getKey().onPlayerLandsOnGround(player, event, entry.getValue());
            }
        }
    }

    public static void onPlayerDropKey(Player player){
        if(playerStats.containsKey(player.getUniqueId())){
            for(Map.Entry<ItemStat, Double> entry : playerStats.get(player.getUniqueId()).getItemStats()){
                entry.getKey().onPlayerDropKeyEvent(player, entry.getValue());
            }
        }
    }

    public static void onPlayerItemBreak(Player player, PlayerItemBreakEvent event){
        if(playerStats.containsKey(player.getUniqueId())){
            for(Map.Entry<ItemStat, Double> entry : playerStats.get(player.getUniqueId()).getItemStats()){
                entry.getKey().onPlayerItemBreak(player, event, entry.getValue());
            }
        }
    }

    public static void onKill(Player player, DamageEvent event, LivingEntity damagee){
        if(playerStats.containsKey(player.getUniqueId())){
            for(Map.Entry<ItemStat, Double> entry : playerStats.get(player.getUniqueId()).getItemStats()){
                entry.getKey().onKill(player, event, damagee, entry.getValue());
            }
        }
    }



    public static class PlayerItemStats{
        public static class ItemStatData implements Iterable<Map.Entry<ItemStat, Double>> {

            private Map<ItemStat, Double> mMap = new LinkedHashMap<>();

            public void add(ItemStat stat, double value){
                if(value == 0){
                    return;
                }
                Double oldValue = mMap.get(stat);
                if(oldValue == null){
                    mMap.put(stat, value);
                }
                else{
                    double newValue = oldValue + value;
                    if(newValue == 0){
                        mMap.remove(stat);
                    }
                    else{
                        mMap.put(stat, newValue);
                    }
                }
            }

            public void add(Map<ItemStat, Double> stats){
                for(Map.Entry<ItemStat, Double> entry : stats.entrySet()){
                    add(entry.getKey(), entry.getValue());
                }
            }

            public ItemStatData add(ItemStatData stats){
                add(stats.mMap);
                return this;
            }

            @Override
            public ItemStatData clone(){
                ItemStatData data = new ItemStatData();
                data.add(mMap);
                return data;
            }

            public void set(ItemStat stat, double value) {
                if (value != 0) {
                    mMap.put(stat, value);
                } else {
                    mMap.remove(stat);
                }
            }

            public double get(ItemStat stat) {
                if (stat == null) {
                    return 0;
                }
                Double value = mMap.get(stat);
                if(value == null){
                    return stat.getDefaultValue();
                }
                return value;
            }

            public void tick(Player player, boolean twoHz, boolean oneHz){
                for(Map.Entry<ItemStat, Double> entry : mMap.entrySet()){
                    entry.getKey().tick(player, entry.getValue(), twoHz, oneHz);
                }
            }

            public void sort(){
                Map<ItemStat, Double> sortedMap = new LinkedHashMap<>();
                for(ItemStat stat : ItemStatUtils.getAllStats()){
                    Double value = mMap.get(stat);
                    if(value != null){
                        sortedMap.put(stat, value);
                    }
                }
                mMap = sortedMap;
            }

            @NotNull
            @Override
            public Iterator<Map.Entry<ItemStat, Double>> iterator() {
                return mMap.entrySet().iterator();
            }
        }

        private ItemStatData mArmorStats = new ItemStatData();
        private ItemStatData mMainhandStats = new ItemStatData();
        private ItemStatData mFinalStats = new ItemStatData();
        //TODO: Add mainhand stats
        public PlayerItemStats(Player player) {
            //TODO: write constructor
        }

        public ItemStatData getItemStats(){
            return mFinalStats;
        }


        public void updateMainhandStats(Player player, ItemStack newMainhand){
            ItemStatData newMainhandStats = new ItemStatData();

            if(newMainhand != null && !newMainhand.isEmpty() && ItemStatUtils.getSlot(newMainhand) == EquipmentSlot.HAND){
                Map<ItemStat, Double> itemStats = ItemStatUtils.getItemStats(newMainhand);
                newMainhandStats.add(itemStats);
                newMainhandStats.sort();
            }
            mMainhandStats = newMainhandStats;
            updateFinalStats(player);
        }

        public void updateArmorStats(Player player){
            ItemStatData newArmorStats = new ItemStatData();

            //Get a list of items in the proper slot
            List<ItemStack> applicableItems = ItemStatUtils.getItemsInCorrectSlot(player, false);

            //Fetch the stats of the item
            for(ItemStack item : applicableItems){
                Map<ItemStat, Double> itemStats = ItemStatUtils.getItemStats(item);
                //Get enchantments and attributes
                newArmorStats.add(itemStats);
                newArmorStats.sort();
            }
            mArmorStats = newArmorStats;
            updateFinalStats(player);
        }

        public Map<ItemStat, double[]> updateFinalStats(Player player){
            ItemStatData oldFinalStats = mFinalStats;
            mFinalStats = mArmorStats.clone().add(mMainhandStats);

            Set<ItemStat> allStats = new LinkedHashSet<>();
            for (Map.Entry<ItemStat, Double> entry : oldFinalStats) allStats.add(entry.getKey());
            for (Map.Entry<ItemStat, Double> entry : mFinalStats) allStats.add(entry.getKey());

            Map<ItemStat, double[]> changes = new LinkedHashMap<>();
            for (ItemStat stat : allStats) {
                double oldValue = oldFinalStats.get(stat);
                double newValue = mFinalStats.get(stat);
                if (oldValue != newValue) {
                    changes.put(stat, new double[]{oldValue, newValue});
                    if (newValue == 0 && oldValue != 0) stat.onRemove(player, oldValue);
                    if (oldValue == 0 && newValue != 0) stat.onAdd(player, newValue);
                }
            }
            return changes;
        }

        public void tick(Player player, boolean twoHz, boolean oneHz){
            mFinalStats.tick(player, twoHz, oneHz);
        }
    }

}
