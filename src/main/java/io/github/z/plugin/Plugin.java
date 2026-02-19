package io.github.z.plugin;

import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.commands.*;
import io.github.z.plugin.itemstats.ItemStat;
import io.github.z.plugin.itemstats.ItemStatUtils;
import io.github.z.plugin.listeners.DamageListener;
import io.github.z.plugin.listeners.GUIListener;
import io.github.z.plugin.listeners.LoginLogoutListener;
import io.github.z.plugin.listeners.PlayerListener;
import io.github.z.plugin.sidebar.SidebarManager;
import io.github.z.plugin.utils.ProjectileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin {

    private static ProjectileUtils projectileUtils;
    private static AbilityManager mAbilityManager;
    private static SidebarManager mSidebarManager;
    private static Plugin plugin;

    public static Plugin getPlugin(){
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        // Plugin startup logic

        Bukkit.getPluginManager().registerEvents(new LoginLogoutListener(), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);

        //Set up utils.
        //TODO: Uncomment
        mAbilityManager = new AbilityManager();
        projectileUtils = new ProjectileUtils();
        mSidebarManager = new SidebarManager();

        //Register commands

        new SetItemAttributeCommand().register();
        new SetItemEnchantmentCommand().register();
        new UpdateItemCommand().register();
        new SetSlotCommand().register();
        new OpenClassGUICommand().register();
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
