package com.tazos.plugin;

import com.tazos.plugin.data.TazoDataManager;
import com.tazos.plugin.gui.TazoAlbumGUI;
import com.tazos.plugin.items.TazoManager;
import com.tazos.plugin.listeners.AlbumGUIListener;
import com.tazos.plugin.listeners.SpiderDeathListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TazosPlugin extends JavaPlugin {

    private TazoManager tazoManager;
    private TazoDataManager dataManager;
    private TazoAlbumGUI albumGUI;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.tazoManager = new TazoManager(this);
        this.dataManager = new TazoDataManager(this);
        this.albumGUI = new TazoAlbumGUI(tazoManager, dataManager);

        getServer().getPluginManager().registerEvents(new SpiderDeathListener(this, tazoManager, dataManager), this);
        getServer().getPluginManager().registerEvents(new AlbumGUIListener(albumGUI), this);

        getLogger().info("TazoDrops habilitado. " + tazoManager.getTazoCount() + " tazo(s) cargado(s).");
    }

    @Override
    public void onDisable() {
        getLogger().info("TazoDrops deshabilitado.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("tazodrops")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                tazoManager.reload();
                sender.sendMessage("§a[TazoDrops] Configuración recargada. " + tazoManager.getTazoCount() + " tazo(s) cargado(s).");
                return true;
            }
            sender.sendMessage("§eUso: /tazodrops reload");
            return true;
        }

        if (command.getName().equalsIgnoreCase("tazos")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cEste comando solo lo puede usar un jugador dentro del juego.");
                return true;
            }
            Player player = (Player) sender;
            albumGUI.open(player, 0);
            return true;
        }

        return false;
    }

    public TazoManager getTazoManager() {
        return tazoManager;
    }

    public TazoDataManager getDataManager() {
        return dataManager;
    }
}
