package io.github.z.plugin.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class GUI implements InventoryHolder {
    private Map<Player, GUI> playerGUIMap = new HashMap<>();
    private Player mPlayer;
    private Inventory mInventory;

    private List<GUIButton> mButtons = new ArrayList<>();
    private Material mPlaceholderMaterial;
    private Component mTitle;

    @Override
    public @NotNull Inventory getInventory() {
        return mInventory;
    }

    protected Player getPlayer(){
        return mPlayer;
    }

    public GUI(Player player, int size){
        this(player, size,  Component.text("Placeholder Inventory Title"));
    }
    public GUI(Player player, int size, Component title){
        this(player, size,  title, Material.BLACK_STAINED_GLASS_PANE);
    }

    public GUI(Player player, int size, Component title, Material placeholder){
        mInventory = Bukkit.createInventory(this, size, title);
        mPlayer = player;
        mPlaceholderMaterial = placeholder;
        playerGUIMap.put(player, this);
        for(int i=0; i < size; i++){
            mButtons.add(null);
        }
        setupGUI();
        buildMenu();
        mPlayer.openInventory(mInventory);
    }

    /**
     * Constructor that skips setupGUI(), buildMenu(), and openInventory().
     * Use this when a subclass needs to initialize its own fields before setupGUI() runs.
     * The subclass must call setupGUI(), buildMenu(), and open() itself after field assignment.
     */
    protected GUI(Player player, int size, Component title, Material placeholder, boolean autoInit){
        mInventory = Bukkit.createInventory(this, size, title);
        mPlayer = player;
        mPlaceholderMaterial = placeholder;
        playerGUIMap.put(player, this);
        for(int i=0; i < size; i++){
            mButtons.add(null);
        }
    }

    public void open(){
        buildMenu();
        mPlayer.openInventory(mInventory);
    }

    public void onClose(){
        playerGUIMap.remove(mPlayer);
    }

    protected void setupGUI(){
        return;
    }

    public void onGUIClick(InventoryClickEvent event){
        int slot = event.getSlot();
        if(slot < mButtons.size() && mButtons.get(slot) != null){
            mButtons.get(slot).onClick(event);
        }
    }
    public void onPlayerInventoryClick(InventoryClickEvent event){

    }
    public void onOtherClick(InventoryClickEvent event){

    }

    public void addButton(GUIButton button, int slot){
        mButtons.set(slot, button);
    }

    public void buildMenu(){
        if(mInventory == null){
            return;
        }
        for(int i=0; i < mButtons.size(); i++){
            GUIButton button = mButtons.get(i);
            if(button == null){
                mInventory.setItem(i, new ItemStack(mPlaceholderMaterial));
            }
            else{
                mInventory.setItem(i, button.getItemStack());
            }
        }
    }

    public void fullRebuild(){
        setupGUI();
        buildMenu();
    }


}
