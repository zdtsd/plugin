package io.github.z.plugin.itemstats;

import com.google.common.collect.Multimap;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import io.github.z.plugin.itemstats.enchantments.TestingOne;
import io.github.z.plugin.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
public class ItemStatUtils {

    private static final String itemStatNBTTag = "itemStats";
    private static final String modifiedDataNBTTag = "itemData";
    private static final String dataValueNBTTag = "dataValue";
    private static final String itemAttrNBTTag = "itemAttr";
    private static final String itemEnchNBTTag = "itemEnch";
    private static final String attrValueTag = "attrValue";
    private static final String enchValueTag = "enchValue";
    private static final String itemSlotTag = "itemSlot";

    //Namespaced keys for attributes
    private static final NamespacedKey armorKey = new NamespacedKey("dummy", "armor");


    private static final List<ItemStat> ITEM_STATS = new ArrayList<>();
    static{
        for(AttributeType type : AttributeType.values()){
            if(!type.isVanilla()){
                ITEM_STATS.add(type.getStat());
            }
        }
        for(EnchantmentType type : EnchantmentType.values()){
            if(!type.isVanilla()) {
                ITEM_STATS.add(type.getItemStat());
            }
        }
        ITEM_STATS.sort(Comparator.comparingDouble(ItemStat::getPriorityAmount));
    }

    public static List<ItemStat> getAllStats(){
        return ITEM_STATS;
    }

    //TODO: Add other head wearables
    private static final Set<Material> headItems = EnumSet.of(
            Material.LEATHER_HELMET,
            Material.CHAINMAIL_HELMET,
            Material.GOLDEN_HELMET,
            Material.IRON_HELMET,
            Material.DIAMOND_HELMET,
            Material.NETHERITE_HELMET
    );

    private static final Set<Material> chestItems = EnumSet.of(
            Material.LEATHER_CHESTPLATE,
            Material.CHAINMAIL_CHESTPLATE,
            Material.GOLDEN_CHESTPLATE,
            Material.IRON_CHESTPLATE,
            Material.DIAMOND_CHESTPLATE,
            Material.NETHERITE_CHESTPLATE
    );
    private static final Set<Material> legsItems = EnumSet.of(
            Material.LEATHER_LEGGINGS,
            Material.CHAINMAIL_LEGGINGS,
            Material.GOLDEN_LEGGINGS,
            Material.IRON_LEGGINGS,
            Material.DIAMOND_LEGGINGS,
            Material.NETHERITE_LEGGINGS
    );
    private static final Set<Material> feetItems = EnumSet.of(
            Material.LEATHER_BOOTS,
            Material.CHAINMAIL_BOOTS,
            Material.GOLDEN_BOOTS,
            Material.IRON_BOOTS,
            Material.DIAMOND_BOOTS,
            Material.NETHERITE_BOOTS
    );
    public static boolean defaultIsHead(ItemStack item){
        return headItems.contains(item.getType());
    }

    public static boolean defaultIsChest(ItemStack item){
        return chestItems.contains(item.getType());
    }
    public static boolean defaultIsLegs(ItemStack item){
        return legsItems.contains(item.getType());
    }
    public static boolean defaultIsFeet(ItemStack item){
        return feetItems.contains(item.getType());
    }

    //Returns a list of the items in their proper slot to grant stats to the player.
    public static List<ItemStack> getItemsInCorrectSlot(Player player){
        ItemStack mainhand = player.getInventory().getItem(EquipmentSlot.HAND);
        ItemStack offhand = player.getInventory().getItem(EquipmentSlot.OFF_HAND);
        ItemStack head = player.getInventory().getItem(EquipmentSlot.HEAD);
        ItemStack chest = player.getInventory().getItem(EquipmentSlot.CHEST);
        ItemStack legs = player.getInventory().getItem(EquipmentSlot.LEGS);
        ItemStack feet = player.getInventory().getItem(EquipmentSlot.FEET);

        List<ItemStack> applicableItems = new ArrayList<>();
        if(!head.isEmpty() && getSlot(head) == EquipmentSlot.HEAD){
            applicableItems.add(head);
        }
        if(!chest.isEmpty() && getSlot(chest) == EquipmentSlot.CHEST){
            applicableItems.add(chest);
        }
        if(!legs.isEmpty() && getSlot(legs) == EquipmentSlot.LEGS){
            applicableItems.add(legs);
        }
        if(!feet.isEmpty() && getSlot(feet) == EquipmentSlot.FEET){
            applicableItems.add(feet);
        }
        if(!offhand.isEmpty() && getSlot(offhand) == EquipmentSlot.OFF_HAND){
            applicableItems.add(offhand);
        }
        if(!mainhand.isEmpty() && getSlot(mainhand) == EquipmentSlot.HAND){
            applicableItems.add(mainhand);
        }

        return applicableItems;
    }


    /*
    * The itemStatNBTTag contains the itemEnch and itemAttr NBT tags
    * Each individual stat is a compound tag belonging to the ench or attr compound tags.
    * Each stat tag contains a value tag which contains a double.
    *
    * */

    public static Map<ItemStat, Double> getItemStats(ItemStack item){

        Map<ItemStat, Double> itemStats = new HashMap<>();

        //Return if invalid item
        if(item == null || item.isEmpty() || item.getType() == Material.AIR){
            return itemStats;
        }
        //Get the NBT that stores item stats
        return NBT.get(item, nbt -> {
            ReadableNBT itemStatNBT = nbt.getCompound(itemStatNBTTag);
            if(itemStatNBT != null){
                for(ItemStat stat : ITEM_STATS){
                    if(stat instanceof Attribute){
                        double statValue = getItemAttr(item, stat.getName());
                        if(statValue != 0){
                            itemStats.put(stat, statValue);
                        }
                    }
                    else if(stat instanceof Enchantment){
                        double statValueEnch = getItemEnch(item, stat.getName());
                        if(statValueEnch != 0){
                            itemStats.put(stat, statValueEnch);
                        }
                    }
                }
            }
            return itemStats;
        });
        //TODO: setup deeper NBT structure
    }


    public static double getItemAttr(ItemStack item, String statName){
        if(item == null || item.isEmpty() || item.getType() == Material.AIR){
            return 0;
        }
        String nbtPath = itemAttrNBTTag;
        return NBT.get(item, nbt -> {
            try{
                return nbt.getCompound(itemStatNBTTag).getCompound(nbtPath).getCompound(statName).getDouble(attrValueTag);
            }
            catch (NullPointerException e){
                return 0.0;
            }
        });
    }

    public static double getItemEnch(ItemStack item, String statName){
        if(item == null || item.isEmpty() || item.getType() == Material.AIR){
            return 0;
        }
        return NBT.get(item, nbt -> {
            try{
                return nbt.getCompound(itemStatNBTTag).getCompound(itemEnchNBTTag).getCompound(statName).getDouble(enchValueTag);
            }
            catch (NullPointerException e){
                return 0.0;
            }
        });
    }
    public static void setItemAttr(ItemStack item, String attrName, double value){
        setItemAttr(item, attrName, value, true);
    }
    public static void setItemAttr(ItemStack item, String attrName, double value, boolean updateItem){
        if(item == null || item.isEmpty() || item.getType() == Material.AIR){
            return;
        }

        NBT.modify(item, nbt -> {
            ReadWriteNBT itemAttrNBT = nbt.getOrCreateCompound(itemStatNBTTag).getOrCreateCompound(itemAttrNBTTag);
            if(value == 0){
                itemAttrNBT.removeKey(attrName);
            }
            else{
                itemAttrNBT.getOrCreateCompound(attrName).setDouble(attrValueTag, value);
            }
        });
        if(updateItem){updateItem(item);}
    }

    public static void setItemEnch(ItemStack item, String enchName, double value){
        setItemEnch(item, enchName, value, true);
    }
    public static void setItemEnch(ItemStack item, String enchName, double value, boolean updateItem){
        if(item == null || item.isEmpty() || item.getType() == Material.AIR){
            return;
        }
        NBT.modify(item, nbt -> {
            ReadWriteNBT itemAttrNBT = nbt.getOrCreateCompound(itemStatNBTTag).getOrCreateCompound(itemEnchNBTTag);
            if(value == 0){
                itemAttrNBT.removeKey(enchName);
            }
            else{
                itemAttrNBT.getOrCreateCompound(enchName).setDouble(enchValueTag, value);
            }
        });
        if(updateItem){updateItem(item);}
    }

    public static void setItemData(ItemStack item, String dataName, double value){
        if(item == null || item.isEmpty() || item.getType() == Material.AIR){
            return;
        }
        NBT.modify(item, nbt -> {
            ReadWriteNBT itemDataNBT = nbt.getOrCreateCompound(modifiedDataNBTTag);
            if(value == 0){
                itemDataNBT.removeKey(dataName);
            }
            else{
                itemDataNBT.getOrCreateCompound(dataName).setDouble(dataValueNBTTag, value);
            }
        });
    }

    public static double getItemData(ItemStack item, String dataName){
        if(item == null || item.isEmpty() || item.getType() == Material.AIR){
            return 0;
        }
        return NBT.get(item, nbt -> {
            try{
                return nbt.getCompound(modifiedDataNBTTag).getCompound(dataName).getDouble(dataValueNBTTag);
            }
            catch (NullPointerException e){
                return 0.0;
            }
        });
    }

    public static ItemStack getHeldItem(Player player){
        return player.getInventory().getItem(EquipmentSlot.HAND);
    }

    public static void updateItemLore(ItemStack item){
        List<Component> lore = new ArrayList<>();

        for(EnchantmentType type : EnchantmentType.values()){
            double rawLevel = getItemEnch(item, type.getName());
            int level = (int) Math.round(rawLevel);
            if(level != 0 && type.getDisplayName() != ""){
                Component line = Component.text(type.getDisplayName() + " " + integerToRomanNumeral(level))
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false);
                lore.add(line);
            }
        }
        lore.add(Component.text(""));
        lore.add(Component.text(""));

        EquipmentSlot slot = getSlot(item);
        String slotText = switch (slot) {
            case null -> "ERROR: NO SLOT SELECTED";
            case HAND -> "When in mainhand: ";
            case OFF_HAND -> "When in offhand: ";
            case FEET -> "When on feet: ";
            case LEGS -> "When on legs: ";
            case CHEST -> "When on chest: ";
            case HEAD -> "When on head: ";
            case BODY -> "ERROR: BODY is not a valid player slot!";
        };

        lore.add(Component.text(slotText)
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false));

        //TODO: record and detect correct slot
        for(AttributeType type : AttributeType.values()){
            //Get level
            double rawLevel = getItemAttr(item, type.getName());

            //Set lore
            if(rawLevel != 0 && type.getDisplayName() != ""){
                //Round raw level.
                String levelDisplay = rawLevel % 1 != 0 ? "" + Math.abs(rawLevel) : "" + (int) Math.abs(rawLevel);

                //Display % changes differently
                String name = type.getDisplayName();
                if(!name.startsWith("%")){
                    levelDisplay += " ";
                }
                //Select operation and color
                String operation  = "";
                NamedTextColor color = NamedTextColor.DARK_GREEN;
                if(!type.isBaseStat()){
                    operation = rawLevel > 0 ? "+" : "-";
                    color = rawLevel > 0 ? NamedTextColor.BLUE : NamedTextColor.RED;
                }


                Component line = Component.text( operation + levelDisplay  + name)
                        .color(color)
                        .decoration(TextDecoration.ITALIC, false);
                lore.add(line);
            }


        }
        item.lore(lore);
        item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }
    public static void updateItemAttributesAndEnchantments(ItemStack item){
        //TODO: Add a slot detector for armor that applies the proper tag if the tag does not exist
        //Get the slot; items without slots are invalid.
        EquipmentSlot slot = getSlot(item);
        if(slot == null){
            return;
        }

        //Remove and refresh enchantments
        item.removeEnchantments();
        for(EnchantmentType type : EnchantmentType.values()){
            if(type.isVanilla()){
                //Get the level of the enchantment.
                int level = (int) getItemEnch(item, type.getName());
                //Skip enchantments with total level 0.
                if(level == 0){
                    continue;
                }
                //Add the enchantment
                item.addUnsafeEnchantment(type.getVanillaStat(), level);
            }
        }

        //Clean and refresh attributes
        NBT.modify(item, nbt -> {
            nbt.modifyMeta((nbtr, meta) -> {
                meta.setUnbreakable(true);

                //Remove all attributes
                meta.setAttributeModifiers(null);

                //Add a dummy 0 armor tag
                meta.addAttributeModifier(org.bukkit.attribute.Attribute.GENERIC_ARMOR,
                        new AttributeModifier(armorKey, 0, AttributeModifier.Operation.ADD_NUMBER));

                //Add each nonzero vanilla attribute to the item.
                for(AttributeType type : AttributeType.values()){
                    if(type.isVanilla()){
                        double attr = getItemAttr(item, type.getName());
                        if(attr == 0){
                            continue;
                        }
                        //Set operation based on if attribute is a percentage
                        AttributeModifier.Operation operation = type.isVanillaPercentage() ? AttributeModifier.Operation.ADD_SCALAR : AttributeModifier.Operation.ADD_NUMBER;
                        if(operation == AttributeModifier.Operation.ADD_SCALAR){
                            attr /= 100.0;
                        }
                        meta.addAttributeModifier(type.getVanillaStat(),
                                new AttributeModifier(new NamespacedKey(slot.getGroup().toString(), type.getName()),
                                        attr,
                                        operation,
                                        slot.getGroup()));
                    }
                }
            });
        });


    }

    private static void itemOnUpdate(ItemStack item){
        for(ItemStat stat : ITEM_STATS){
            Map<ItemStat, Double> itemStats = getItemStats(item);
            if(itemStats.containsKey(stat) && itemStats.get(stat) != 0){
                stat.onUpdate(item, itemStats.get(stat));
            }
        }
    }
    public static void updateItem(ItemStack item){
        itemOnUpdate(item);
        updateItemLore(item);
        updateItemAttributesAndEnchantments(item);
        if(getSlot(item) == null){
            if(defaultIsHead(item)){
                setSlot(EquipmentSlot.HEAD, item);
            }
            else if(defaultIsChest(item)){
                setSlot(EquipmentSlot.CHEST, item);
            }
            else if(defaultIsLegs(item)){
                setSlot(EquipmentSlot.LEGS, item);
            }
            else if(defaultIsFeet(item)){
                setSlot(EquipmentSlot.FEET, item);
            }
        }
    }

    public static void setSlot(EquipmentSlot slot, ItemStack item){
        NBT.modify(item, nbt -> {
            nbt.getOrCreateCompound(itemStatNBTTag).setString(itemSlotTag, slot.toString());
        });
        updateItem(item);
    }

    public static EquipmentSlot getSlot(ItemStack item){
        return NBT.get(item, nbt -> {
            ReadableNBT statNBT = nbt.getCompound(itemStatNBTTag);
            if(statNBT != null){
                String slotName = statNBT.getString(itemSlotTag);
                for(EquipmentSlot slot : EquipmentSlot.values()){
                    if(slot.toString().equalsIgnoreCase(slotName)){
                        return slot;
                    }
                }
            }
            return null;
        });
    }

    public static String integerToRomanNumeral(int input) {
        if (input < 1 || input > 3999)
            return "Invalid Roman Number Value";
        String s = "";
        while (input >= 1000) {
            s += "M";
            input -= 1000;        }
        while (input >= 900) {
            s += "CM";
            input -= 900;
        }
        while (input >= 500) {
            s += "D";
            input -= 500;
        }
        while (input >= 400) {
            s += "CD";
            input -= 400;
        }
        while (input >= 100) {
            s += "C";
            input -= 100;
        }
        while (input >= 90) {
            s += "XC";
            input -= 90;
        }
        while (input >= 50) {
            s += "L";
            input -= 50;
        }
        while (input >= 40) {
            s += "XL";
            input -= 40;
        }
        while (input >= 10) {
            s += "X";
            input -= 10;
        }
        while (input >= 9) {
            s += "IX";
            input -= 9;
        }
        while (input >= 5) {
            s += "V";
            input -= 5;
        }
        while (input >= 4) {
            s += "IV";
            input -= 4;
        }
        while (input >= 1) {
            s += "I";
            input -= 1;
        }
        return s;
    }
}
