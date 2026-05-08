package io.github.z.plugin.libraryofsouls;

import io.github.z.plugin.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BookOfSoulsUtils {

    public static final NamespacedKey BOS_MARKER_KEY = new NamespacedKey(Plugin.getPlugin(), "book_of_souls");
    public static final NamespacedKey BOS_ID_KEY = new NamespacedKey(Plugin.getPlugin(), "book_of_souls_id");
    public static final NamespacedKey SPAWNER_SOUL_KEY = new NamespacedKey(Plugin.getPlugin(), "spawner_soul_id");

    public static ItemStack createBookOfSouls(String id) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(id).decoration(TextDecoration.ITALIC, false));

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(BOS_MARKER_KEY, PersistentDataType.BYTE, (byte) 1);
        pdc.set(BOS_ID_KEY, PersistentDataType.STRING, id);

        item.setItemMeta(meta);
        return item;
    }

    public static String getBookOfSoulsID(ItemStack item) {
        if (!isBookOfSouls(item)) return null;
        return item.getItemMeta().getPersistentDataContainer().get(BOS_ID_KEY, PersistentDataType.STRING);
    }

    public static boolean isBookOfSouls(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(BOS_MARKER_KEY, PersistentDataType.BYTE);
    }
}
