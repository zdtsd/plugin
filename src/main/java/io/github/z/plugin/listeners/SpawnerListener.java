package io.github.z.plugin.listeners;

import io.github.z.plugin.libraryofsouls.BookOfSoulsUtils;
import io.github.z.plugin.libraryofsouls.LibraryOfSoulsAPI;
import io.github.z.plugin.libraryofsouls.Soul;
import org.bukkit.Bukkit;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.persistence.PersistentDataType;

public class SpawnerListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onSpawnerSpawn(SpawnerSpawnEvent event) {
        CreatureSpawner spawner = event.getSpawner();
        assert spawner != null;
        String soulId = spawner.getPersistentDataContainer().get(BookOfSoulsUtils.SPAWNER_SOUL_KEY, PersistentDataType.STRING);
        Bukkit.getLogger().info("Spawner not null.");
        if (soulId == null) return;
        Bukkit.getLogger().info("SoulID not null.");

        Soul soul = LibraryOfSoulsAPI.getInstance().getDatabase().getSoul(soulId);
        if (soul == null) return;
        Bukkit.getLogger().info("Soul not null.");

        event.setCancelled(true);
        LibraryOfSoulsAPI.getInstance().summon(event.getEntity().getLocation(), soul);
    }
}
