package io.github.z.plugin.listeners;

import io.github.z.plugin.utils.PlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LoginLogoutListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        PlayerUtils.onLogin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        PlayerUtils.onLogout(event.getPlayer());
    }
}
