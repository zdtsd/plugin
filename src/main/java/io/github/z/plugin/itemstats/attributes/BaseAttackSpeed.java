package io.github.z.plugin.itemstats.attributes;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Attribute;
import io.github.z.plugin.itemstats.ItemStatUtils;
import io.github.z.plugin.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BaseAttackSpeed implements Attribute {

    @Override
    public String getName() {
        return "attack_speed_base";
    }

    @Override
    public double getPriorityAmount() {
        return -100;
    }


    @Override
    public void onUpdate(ItemStack item, double level){
        ItemStatUtils.setItemAttr(item, org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED.getKey().getKey(), level - 4, false);
    }
}
