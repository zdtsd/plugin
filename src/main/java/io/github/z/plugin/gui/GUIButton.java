package io.github.z.plugin.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GUIButton {
    private Material mMaterial;
    private int mCount = 1;
    private Component mName;
    private Style mNameStyle = Style.style(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false);
    private final List<Component> mLore = new ArrayList<>();
    private Style mLoreStyle = Style.style(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);
    private final List<Consumer<InventoryClickEvent>> mHandlers = new ArrayList<>();
    private ItemStack mItemStack;

    //Builder functions
    public GUIButton addHandler(Consumer<InventoryClickEvent> handler){
        mHandlers.add(handler);
        return this;
    }

    public GUIButton setMaterial(Material material){
        mMaterial = material;
        return this;
    }

    public GUIButton setCount(int count){
        mCount = count;
        return this;
    }

    public GUIButton setName(Component name){
        mName = name;
        return this;
    }

    public GUIButton setNameStyle(Style style){
        mNameStyle = style;
        return this;
    }

    public GUIButton addLore(Component lore){
        mLore.add(lore);
        return this;
    }
    public GUIButton addLore(List<Component> lore){
        mLore.addAll(lore);
        return this;
    }

    public GUIButton setLoreStyle(Style style){
        mLoreStyle = style;
        return this;
    }

    public void addToGUI(GUI gui, int slot){
        gui.addButton(this, slot);
    }

    public GUIButton buildItemStack(){
        mItemStack = new ItemStack(mMaterial, mCount);
        ItemMeta meta = mItemStack.getItemMeta();

        meta.addItemFlags(ItemFlag.values());

        meta.displayName(mName.style(mNameStyle));
        //TODO: DISPLAY LORE STYLE
        meta.lore(mLore);
        mItemStack.setItemMeta(meta);
        return this;
    }

    public ItemStack getItemStack(){
        if(mItemStack == null){
            buildItemStack();
        }
        return mItemStack;
    }

    public void onClick(InventoryClickEvent event){
        for(Consumer<InventoryClickEvent> consumer : mHandlers){
            consumer.accept(event);
        }
    }
}
