package io.github.z.plugin;

import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.mobspells.MobSpellManager;
import io.github.z.plugin.commands.*;
import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.itemstats.ItemStat;
import io.github.z.plugin.itemstats.ItemStatUtils;
import io.github.z.plugin.listeners.*;
import io.github.z.plugin.protocollib.ProtocolLibManager;
import io.github.z.plugin.sidebar.SidebarManager;
import io.github.z.plugin.utils.DoubleJumpManager;
import io.github.z.plugin.utils.ProjectileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin {

    private static ProtocolLibManager mProtocolLibManager;
    private static ProjectileUtils projectileUtils;
    private static AbilityManager mAbilityManager;
    private static MobSpellManager mMobSpellManager;
    private static SidebarManager mSidebarManager;
    private static DoubleJumpManager mDoubleJumpManager;
    private static EffectManager mEffectManager;
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
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);

        //Set up utils.
        mProtocolLibManager = new ProtocolLibManager();
        mAbilityManager = new AbilityManager();
        mMobSpellManager = new MobSpellManager();
        projectileUtils = new ProjectileUtils();
        mSidebarManager = new SidebarManager();
        mDoubleJumpManager = new DoubleJumpManager();
        mEffectManager = new EffectManager();

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
