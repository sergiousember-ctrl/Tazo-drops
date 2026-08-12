package com.tazos.plugin;

import com.tazos.plugin.items.TazoManager;
import com.tazos.plugin.listeners.SpiderDeathListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class TazosPlugin extends JavaPlugin {

    private TazoManager tazoManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.tazoManager = new TazoManager(this);
        getServer().getPluginManager().registerEvents(new SpiderDeathListener(this, tazoManager), this);
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
        return false;
    }

    public TazoManager getTazoManager() {
        return tazoManager;
    }
}
