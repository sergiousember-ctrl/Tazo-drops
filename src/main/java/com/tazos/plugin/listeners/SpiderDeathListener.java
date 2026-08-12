package com.tazos.plugin.listeners;

import com.tazos.plugin.data.TazoDataManager;
import com.tazos.plugin.items.Tazo;
import com.tazos.plugin.items.TazoManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class SpiderDeathListener implements Listener {

    private final JavaPlugin plugin;
    private final TazoManager tazoManager;
    private final TazoDataManager dataManager;

    public SpiderDeathListener(JavaPlugin plugin, TazoManager tazoManager, TazoDataManager dataManager) {
        this.plugin = plugin;
        this.tazoManager = tazoManager;
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        boolean includeCaveSpider = plugin.getConfig().getBoolean("include-cave-spider", false);

        boolean isValidSpider = entity.getType() == EntityType.SPIDER
                || (includeCaveSpider && entity.getType() == EntityType.CAVE_SPIDER);

        if (!isValidSpider) return;

        Player killer = entity.getKiller();
        if (killer == null) return;

        double chance = plugin.getConfig().getDouble("drop-chance", 5.0);
        double roll = ThreadLocalRandom.current().nextDouble(100.0);
        if (roll >= chance) return;

        Tazo tazo = tazoManager.getRandomTazo();
        if (tazo == null) return;

        ItemStack tazoItem = tazoManager.createTazoItem(tazo);
        dataManager.addCollected(killer, tazo.getId());

        if (plugin.getConfig().getBoolean("give-directly", true)) {
            Map<Integer, ItemStack> leftover = killer.getInventory().addItem(tazoItem);
            leftover.values().forEach(item -> killer.getWorld().dropItemNaturally(killer.getLocation(), item));
        } else {
            entity.getWorld().dropItemNaturally(entity.getLocation(), tazoItem);
        }

        String message = plugin.getConfig().getString("win-message", "&6Obtuviste un Tazo numero %id% - %name%!")
                .replace("%id%", String.valueOf(tazo.getId()))
                .replace("%name%", tazo.getName());
        killer.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
