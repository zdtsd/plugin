package io.github.z.plugin.itemstats;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableItemNBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import io.github.z.plugin.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
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













    public static class PlayerItemStats{
        public static class ItemStatData implements Iterable<Map.Entry<ItemStat, Double>> {

            private final Map<ItemStat, Double> mMap = new LinkedHashMap<>();

            public void add(ItemStat stat, double value){
                Bukkit.getLogger().info(value + "");
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
                List<Map<ItemStat, Double>> itemStats = ItemStatUtils.getItemStats(item);
                //Get enchantments and attributes
                newArmorStats.add(itemStats.get(0));
                newArmorStats.add(itemStats.get(1));
            }
            mFinalStats = newArmorStats;

        }

        public void tick(Plugin plugin, Player player, boolean twoHz, boolean oneHz){
            mFinalStats.tick(plugin, player, twoHz, oneHz);
        }
    }

}
