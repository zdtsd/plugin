package io.github.z.plugin.protocollib;

import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayInBlockDigHandle;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import io.github.z.plugin.listeners.PlayerListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class DropKeyPacketListener extends PacketAdapter {
    public DropKeyPacketListener(Plugin plugin) {
        super(plugin, PacketType.Play.Client.BLOCK_DIG);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketPlayInBlockDigHandle packet = PacketPlayInBlockDigHandle.createHandle(event.getPacket().getHandle());
        PacketPlayInBlockDigHandle.EnumPlayerDigTypeHandle digTypeHandle = packet.getDigType();
        if(PacketPlayInBlockDigHandle.EnumPlayerDigTypeHandle.DROP_ALL_ITEMS.equals(digTypeHandle)
                || PacketPlayInBlockDigHandle.EnumPlayerDigTypeHandle.DROP_ITEM.equals(digTypeHandle)){
            //Cancel drop
            event.setCancelled(true);
            Player player = event.getPlayer();
            player.updateInventory();

            //Register drop (should be thread safe?)
            PlayerListener.registerItemDrop(player);
        }
    }
}
