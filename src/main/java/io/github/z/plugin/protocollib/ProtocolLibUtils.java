package io.github.z.plugin.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;

public class ProtocolLibUtils {

    /**
     * Sends the player a clickable chat message that, when clicked, pre-fills
     * the chat bar with the given text. Uses ProtocolLib to construct and send
     * the SYSTEM_CHAT packet directly.
     */
    public static void autofillChat(Player player, String text) {
        Component message = Component.text("Click to run: ", NamedTextColor.GRAY)
                .append(Component.text(text, NamedTextColor.YELLOW)
                        .clickEvent(ClickEvent.suggestCommand(text)));

        String json = GsonComponentSerializer.gson().serialize(message);

        PacketContainer packet = ProtocolLibrary.getProtocolManager()
                .createPacket(PacketType.Play.Server.SYSTEM_CHAT);
        packet.getChatComponents().write(0, WrappedChatComponent.fromJson(json));
        packet.getBooleans().write(0, false); // false = not an overlay (hotbar) message

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (Exception e) {
            // Fallback: send via Adventure API directly if packet fails
            player.sendMessage(message);
        }
    }
}
