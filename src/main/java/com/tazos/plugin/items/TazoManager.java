package com.tazos.plugin.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TazoManager {

    private final JavaPlugin plugin;
    private final List<Tazo> tazos = new ArrayList<>();
    private final Random random = new Random();

    public TazoManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadTazos();
    }

    private void loadTazos() {
        tazos.clear();
        List<?> rawList = plugin.getConfig().getList("tazos");
        if (rawList == null) return;

        for (Object obj : rawList) {
            if (!(obj instanceof java.util.Map)) continue;
            java.util.Map<?, ?> map = (java.util.Map<?, ?>) obj;

            try {
                int id = Integer.parseInt(String.valueOf(map.get("id")));
                String name = String.valueOf(map.get("name"));
                String type = String.valueOf(map.get("type"));
                String imageUrl = String.valueOf(map.get("image-url"));
                tazos.add(new Tazo(id, name, type, imageUrl));
            } catch (Exception e) {
                plugin.getLogger().warning("Tazo mal configurado en config.yml, revisa el formato: " + map);
            }
        }
    }

    public void reload() {
        loadTazos();
    }

    public int getTazoCount() {
        return tazos.size();
    }
    public List<Tazo> getAllTazos() {
        return tazos;
    }

    public Tazo getRandomTazo() {
        if (tazos.isEmpty()) return null;
        return tazos.get(random.nextInt(tazos.size()));
    }

    public ItemStack createTazoItem(Tazo tazo) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta == null) return item;

        try {
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "tazo_" + tazo.getId());
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(new URL(tazo.getImageUrl()));
            profile.setTextures(textures);
            meta.setOwnerProfile(profile);
        } catch (MalformedURLException e) {
            plugin.getLogger().warning("URL de imagen inválida para el tazo #" + tazo.getId() + ": " + tazo.getImageUrl());
        }

        meta.setDisplayName(ChatColor.GOLD + "Tazo #" + tazo.getId() + " - " + tazo.getName());
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Tipo: " + ChatColor.AQUA + tazo.getType());
        lore.add(ChatColor.YELLOW + "¡Atrápalo ya!");
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }
}
