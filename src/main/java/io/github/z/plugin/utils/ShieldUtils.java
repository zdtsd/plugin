package io.github.z.plugin.utils;

import de.tr7zw.nbtapi.NBT;
import io.github.z.plugin.itemstats.AttributeType;
import io.github.z.plugin.itemstats.ItemStatManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ShieldUtils {
    public static final int SHIELD_BREAK_COOLDOWN = 3 * 20, SHIELD_DURABILITY = 336;

    public static void damageShield(Player player, double damage){

        ItemStack shieldItem = getShield(player);
        if(shieldItem == null){return;}


        Damageable metaDamageable = (Damageable) shieldItem.getItemMeta();

        int originalDamage = metaDamageable.getDamage();
        int newDamage = (int) Math.round(SHIELD_DURABILITY * damage / getShieldHealth(player));
        int finalDamage = newDamage + originalDamage;

        if(finalDamage >= SHIELD_DURABILITY){
            finalDamage = 0;
            player.setCooldown(Material.SHIELD, SHIELD_BREAK_COOLDOWN);
        }

        metaDamageable.setDamage(finalDamage);
        shieldItem.setItemMeta(metaDamageable);
    }

    public static void healShield(Player player, double healing){
        ItemStack shieldItem = getShield(player);
        if(shieldItem == null){return;}


        Damageable metaDamageable = (Damageable) shieldItem.getItemMeta();

        int originalDamage = metaDamageable.getDamage();
        int damageHealed = (int) Math.round(SHIELD_DURABILITY * healing / getShieldHealth(player));
        int finalDamage = originalDamage - damageHealed;

        if(finalDamage < 0){
            finalDamage = 0;
        }

        metaDamageable.setDamage(finalDamage);
        shieldItem.setItemMeta(metaDamageable);
    }

    private static double getShieldHealth(Player player){
        double baseHealth = ItemStatManager.getAttribute(player, AttributeType.SHIELD_HEALTH_BASE);
        //TODO: Implement bonus health
        return baseHealth;
    }

    public static ItemStack getShield(Player player){
        if(player.getActiveItem().getType() == Material.SHIELD){
            return player.getActiveItem();
        }
        else if(player.getInventory().getItemInMainHand().getType() == Material.SHIELD){
            return player.getInventory().getItemInMainHand();
        }
        else if(player.getInventory().getItemInOffHand().getType() == Material.SHIELD){
            return player.getInventory().getItemInOffHand();
        }
        else{
            return null;
        }
    }
}
