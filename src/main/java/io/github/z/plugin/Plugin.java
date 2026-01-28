package io.github.z.plugin;

import io.github.z.plugin.commands.SetItemAttributeCommand;
import io.github.z.plugin.commands.SetItemEnchantmentCommand;
import io.github.z.plugin.commands.SetSlotCommand;
import io.github.z.plugin.commands.UpdateItemCommand;
import io.github.z.plugin.itemstats.ItemStat;
import io.github.z.plugin.itemstats.ItemStatUtils;
import io.github.z.plugin.listeners.DamageListener;
import io.github.z.plugin.listeners.LoginLogoutListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        Bukkit.getPluginManager().registerEvents(new LoginLogoutListener(), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);

        new SetItemAttributeCommand().register();
        new SetItemEnchantmentCommand().register();
        new UpdateItemCommand().register();
        new SetSlotCommand().register();
        Timer.setPlugin(this);

        for(ItemStat stat : ItemStatUtils.getAllStats()){
            Bukkit.getLogger().info(stat.getName());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
