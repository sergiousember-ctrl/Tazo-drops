package com.tazos.plugin.listeners;

import com.tazos.plugin.gui.TazoAlbumGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AlbumGUIListener implements Listener {

    private final TazoAlbumGUI albumGUI;

    public AlbumGUIListener(TazoAlbumGUI albumGUI) {
        this.albumGUI = albumGUI;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (title == null || !title.startsWith(TazoAlbumGUI.TITLE_PREFIX)) return;

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() != Material.ARROW || !clicked.hasItemMeta()) return;

        ItemMeta meta = clicked.getItemMeta();
        String displayName = meta.getDisplayName();
        if (displayName == null) return;

        int currentPage = extractPage(title) - 1;

        if (displayName.contains("anterior")) {
            albumGUI.open(player, currentPage - 1);
        } else if (displayName.contains("siguiente")) {
            albumGUI.open(player, currentPage + 1);
        }
    }

    private int extractPage(String title) {
        try {
            String afterPrefix = title.substring(TazoAlbumGUI.TITLE_PREFIX.length());
            String pageStr = afterPrefix.split("/")[0].trim();
            return Integer.parseInt(pageStr);
        } catch (Exception e) {
            return 1;
        }
    }
}
