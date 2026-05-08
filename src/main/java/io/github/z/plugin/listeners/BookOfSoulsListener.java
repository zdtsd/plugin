package io.github.z.plugin.listeners;

import io.github.z.plugin.libraryofsouls.BookOfSoulsUtils;
import io.github.z.plugin.libraryofsouls.LibraryOfSoulsAPI;
import io.github.z.plugin.libraryofsouls.Soul;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

public class BookOfSoulsListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        if (!BookOfSoulsUtils.isBookOfSouls(player.getInventory().getItemInMainHand())) return;

        String id = BookOfSoulsUtils.getBookOfSoulsID(player.getInventory().getItemInMainHand());
        if (id == null) return;

        switch (event.getAction()) {
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> {
                event.setCancelled(true);
                LibraryOfSoulsAPI.getInstance().openSoulGUI(player, id);
            }
            case LEFT_CLICK_AIR -> {
                event.setCancelled(true);
                Soul soul = LibraryOfSoulsAPI.getInstance().getDatabase().getSoul(id);
                if (soul == null) return;
                LibraryOfSoulsAPI.getInstance().summon(player.getLocation(), soul);
            }
            case LEFT_CLICK_BLOCK -> {
                event.setCancelled(true);
                Block block = event.getClickedBlock();
                if (block != null && block.getType() == Material.SPAWNER) {
                    CreatureSpawner spawner = (CreatureSpawner) block.getState();
                    spawner.getPersistentDataContainer().set(BookOfSoulsUtils.SPAWNER_SOUL_KEY, PersistentDataType.STRING, id);
                    spawner.update();
                    return;
                }
                Soul soul = LibraryOfSoulsAPI.getInstance().getDatabase().getSoul(id);
                if (soul == null) return;
                LibraryOfSoulsAPI.getInstance().summon(player.getLocation(), soul);
            }
        }
    }
}
