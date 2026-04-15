package io.github.z.plugin.gui.GUIs;

import io.github.z.plugin.gui.GUI;
import io.github.z.plugin.gui.GUIButton;
import io.github.z.plugin.libraryofsouls.LibraryOfSoulsAPI;
import io.github.z.plugin.libraryofsouls.Soul;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import java.util.List;

public class LibraryOfSoulsGUI extends GUI {

    public static final int GUI_SIZE = 54;
    private static final int SOULS_PER_PAGE = 45;
    private static final int PREV_SLOT = 45;
    private static final int NEXT_SLOT = 53;

    private final int mPage;

    public LibraryOfSoulsGUI(Player player, int page) {
        super(player, GUI_SIZE, Component.text("Library of Souls"), Material.BLACK_STAINED_GLASS_PANE, false);
        mPage = page;
        setupGUI();
        buildMenu();
        open();
    }

    @Override
    protected void setupGUI() {
        List<Soul> allSouls = LibraryOfSoulsAPI.getInstance().getDatabase().getAllSouls();
        int totalPages = Math.max(1, (int) Math.ceil((double) allSouls.size() / SOULS_PER_PAGE));
        int clampedPage = Math.min(mPage, totalPages - 1);

        int start = clampedPage * SOULS_PER_PAGE;
        int end = Math.min(start + SOULS_PER_PAGE, allSouls.size());
        List<Soul> pageSouls = allSouls.subList(start, end);

        for (int i = 0; i < pageSouls.size(); i++) {
            Soul soul = pageSouls.get(i);
            new GUIButton()
                    .setMaterial(Material.SOUL_SAND)
                    .setName(Component.text(soul.getId()))
                    .addHandler(event -> {
                        if (event.getWhoClicked() instanceof Player player) {
                            LibraryOfSoulsAPI.getInstance().openSoulGUI(player, soul.getId());
                        }
                    })
                    .addToGUI(this, i);
        }

        if (clampedPage > 0) {
            new GUIButton()
                    .setMaterial(Material.ARROW)
                    .setName(Component.text("Previous Page"))
                    .addHandler(event -> {
                        if (event.getWhoClicked() instanceof Player player) {
                            new LibraryOfSoulsGUI(player, clampedPage - 1);
                        }
                    })
                    .addToGUI(this, PREV_SLOT);
        }

        if (clampedPage < totalPages - 1) {
            new GUIButton()
                    .setMaterial(Material.ARROW)
                    .setName(Component.text("Next Page"))
                    .addHandler(event -> {
                        if (event.getWhoClicked() instanceof Player player) {
                            new LibraryOfSoulsGUI(player, clampedPage + 1);
                        }
                    })
                    .addToGUI(this, NEXT_SLOT);
        }
    }
}
