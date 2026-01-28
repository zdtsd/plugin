package io.github.z.plugin.itemstats.enchantments;

import io.github.z.plugin.itemstats.Enchantment;
import io.github.z.plugin.itemstats.ItemStatUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;

public class DepthStrider implements Enchantment {

    private static final double speedPerLevel = 0.2;


    @Override
    public String getName() {
        return "depth_strider";
    }

    @Override
    public void onUpdate(ItemStack item, double level){
        ItemStatUtils.setItemAttr(item, Attribute.GENERIC_WATER_MOVEMENT_EFFICIENCY.getKey().getKey(), level * speedPerLevel, false);
    }
}
