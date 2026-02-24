package io.github.z.plugin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerLandsOnGroundEvent extends Event {

    private Player mPlayer;

    public PlayerLandsOnGroundEvent(Player player){
        mPlayer = player;
    }

    public Player getPlayer(){
        return mPlayer;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList(){
        return HANDLERS;
    }
}
