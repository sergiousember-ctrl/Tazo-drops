package com.tazos.plugin.gui;

import com.tazos.plugin.data.TazoDataManager;
import com.tazos.plugin.items.Tazo;
import com.tazos.plugin.items.TazoManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TazoAlbumGUI {

    public static final String TITLE_PREFIX = ChatColor.DARK_PURPLE + "Album de Tazos - Pag. ";
    private static final int ITEMS_PER_PAGE = 45;

    private final TazoManager tazoManager;
    private final TazoDataManager dataManager;

    public TazoAlbumGUI(TazoManager tazoManager, TazoDataManager dataManager) {
        this.tazoManager = tazoManager;
        this.dataManager = dataManager;
    }

    public void open(Player player, int page) {
        List<Tazo> all = tazoManager.getAllTazos();
        int totalPages = Math.max(1, (int) Math.ceil(all.size() / (double) ITEMS_PER_PAGE));
        if (page < 0) page = 0;
        if (page >= totalPages) page = totalPages - 1;

        Inventory inv = Bukkit.createInventory(null, 54, TITLE_PREFIX + (page + 1) + "/" + totalPages);

        int start = page * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, all.size());

        int slot = 0;
        for (int i = start; i < end; i++) {
            Tazo tazo = all.get(i);
            ItemStack item;
            if (dataManager.hasCollected(player, tazo.getId())) {
                item = tazoManager.createTazoItem(tazo);
            } else {
                item = createLockedItem();
            }
            inv.setItem(slot, item);
            slot++;
        }

        if (page > 0) {
            inv.setItem(45, createNavItem(Material.ARROW, ChatColor.YELLOW + "<< Pagina anterior"));
        }
        if (page < totalPages - 1) {
            inv.setItem(53, createNavItem(Material.ARROW, ChatColor.YELLOW + "Pagina siguiente >>"));
        }

        int collectedCount = 0;
        for (Tazo tazo : all) {
            if (dataManager.hasCollected(player, tazo.getId())) collectedCount++;
        }
        inv.setItem(49, createNavItem(Material.BOOK, ChatColor.GOLD + "Progreso: " + collectedCount + "/" + all.size()));

        player.openInventory(inv);
    }

    private ItemStack createLockedItem() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "??? Tazo sin descubrir");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + "Sigue matando aranas para conseguirlo");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createNavItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
