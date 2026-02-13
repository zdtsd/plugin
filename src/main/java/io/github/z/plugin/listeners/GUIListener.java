package io.github.z.plugin.listeners;

import io.github.z.plugin.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class GUIListener implements Listener {
    @EventHandler(ignoreCancelled = false)
    public void onClick(InventoryClickEvent event){
        if(!(event.getInventory().getHolder(false) instanceof GUI gui)){
            return;
        }

        if(event.getClickedInventory() == gui.getInventory()){
            gui.onGUIClick(event);
        }
        else if(event.getClickedInventory() != null){
            gui.onPlayerInventoryClick(event);
        }
        else{
            gui.onOtherClick(event);
        }

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = false)
    public void onDrag(InventoryDragEvent event){
        if(!(event.getInventory().getHolder(false) instanceof GUI gui)){
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = false)
    public void onClose(InventoryCloseEvent event){
        if(!(event.getInventory().getHolder(false) instanceof GUI gui)){
            return;
        }

        gui.onClose();
    }
}
