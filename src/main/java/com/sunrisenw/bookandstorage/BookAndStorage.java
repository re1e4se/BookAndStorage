package com.sunrisenw.bookandstorage;

import com.sunrisenw.bookandstorage.commands.CreateDataCommand;
import com.sunrisenw.bookandstorage.commands.WriteDataCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class BookAndStorage extends JavaPlugin {
    @Override
    public void onEnable() {
        System.out.println("[BookAndStorage] Welcome! Enabling BookAndStorage...");
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Config.setup();
        Config.get().addDefault("clearInventory", false);
        Config.get().addDefault("splitBooks", true);
        Config.get().addDefault("useShulkers", true);

        Config.get().addDefault("bookPageLimit", 100);
        Config.get().addDefault("inventorySize", true);
        Config.get().options().copyDefaults(true);
        Config.save();
        getCommand("createdata").setExecutor(new CreateDataCommand());
        getCommand("writedata").setExecutor(new WriteDataCommand());
    }

    @Override
    public void onDisable() {
        System.out.println("[BookAndStorage] Goodbye! Disabling BookAndStorage...");
    }
}
