package com.tazos.plugin.data;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TazoDataManager {

    private final JavaPlugin plugin;
    private final File dataFolder;
    private final ConcurrentHashMap<UUID, Set<Integer>> cache = new ConcurrentHashMap<>();

    public TazoDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    private File getFile(UUID uuid) {
        return new File(dataFolder, uuid.toString() + ".yml");
    }

    public Set<Integer> getCollected(Player player) {
        return cache.computeIfAbsent(player.getUniqueId(), this::loadFromDisk);
    }

    private Set<Integer> loadFromDisk(UUID uuid) {
        Set<Integer> collected = new HashSet<>();
        File file = getFile(uuid);
        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            List<Integer> ids = config.getIntegerList("collected");
            collected.addAll(ids);
        }
        return collected;
    }

    public boolean hasCollected(Player player, int tazoId) {
        return getCollected(player).contains(tazoId);
    }

    public void addCollected(Player player, int tazoId) {
        Set<Integer> collected = getCollected(player);
        if (collected.add(tazoId)) {
            saveToDisk(player.getUniqueId(), collected);
        }
    }

    private void saveToDisk(UUID uuid, Set<Integer> collected) {
        File file = getFile(uuid);
        YamlConfiguration config = new YamlConfiguration();
        config.set("collected", new ArrayList<>(collected));
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("No se pudo guardar los datos del jugador " + uuid + ": " + e.getMessage());
        }
    }
}
