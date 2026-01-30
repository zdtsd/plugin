package io.github.z.plugin.itemstats;

import io.github.z.plugin.events.DamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ItemStatManager {

    public static Map<UUID, PlayerItemStats> playerStats = new WeakHashMap<>();


    public static void tick(Plugin plugin, Player player, boolean twoHz, boolean oneHz){
        for(Map.Entry<UUID, PlayerItemStats> entry : playerStats.entrySet()){
            entry.getValue().tick(plugin, player, twoHz, oneHz);
        }
    }

    public static void updateStats(Player player){
        PlayerItemStats stats = playerStats.get(player.getUniqueId());
        if(stats == null){
            stats = new PlayerItemStats(player);
            playerStats.put(player.getUniqueId(), stats);
        }
        stats.updateStats(player);
    }


    public static double getStat(Player player, ItemStat stat){
        return playerStats.get(player.getUniqueId()).getItemStats().get(stat);
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

            public void tick(Plugin plugin, Player player, boolean twoHz, boolean oneHz){
                for(Map.Entry<ItemStat, Double> entry : mMap.entrySet()){
                    entry.getKey().tick(plugin, player, entry.getValue(), twoHz, oneHz);
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

        private ItemStatData mArmorAddStats = new ItemStatData();
        private ItemStatData mArmorMultiplyStats = new ItemStatData();
        private ItemStatData mFinalStats = new ItemStatData();
        //TODO: Add mainhand stats
        public PlayerItemStats(Player player) {
            //TODO: write constructor
        }

        public ItemStatData getItemStats(){
            return mFinalStats;
        }

        public void updateStats(Player player){
            //TODO: Add mainhand functionality
            ItemStatData newArmorStats = new ItemStatData();

            //Get a list of items in the proper slot
            List<ItemStack> applicableItems = ItemStatUtils.getItemsInCorrectSlot(player);

            //Fetch the stats of the item
            for(ItemStack item : applicableItems){
                Map<ItemStat, Double> itemStats = ItemStatUtils.getItemStats(item);
                //Get enchantments and attributes
                newArmorStats.add(itemStats);
                newArmorStats.sort();
            }
            mFinalStats = newArmorStats;

        }

        public void tick(Plugin plugin, Player player, boolean twoHz, boolean oneHz){
            mFinalStats.tick(plugin, player, twoHz, oneHz);
        }
    }

}
