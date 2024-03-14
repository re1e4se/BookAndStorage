package com.sunrisenw.bookandstorage;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private static File file;
    private static FileConfiguration configFile;
    private static File filesDirectory;

    public static void setup(){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("BookAndStorage").getDataFolder(), "config.yml");
        filesDirectory = new File(Bukkit.getServer().getPluginManager().getPlugin("BookAndStorage").getDataFolder(), "/data");

        filesDirectory.mkdirs();
        if (!file.exists()){
            try{
                file.createNewFile();
            } catch (IOException e){

            }
        }

        configFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return configFile;
    }

    public static File getFileInData(String fileName){
        return new File(filesDirectory, fileName);
    }

    public static void save(){
        try{
            configFile.save(file);
        } catch (IOException e){
            System.out.println("[BookAndStorage] ERROR! Couldn't save config file.");
        }
    }

    public static void reload(){
        configFile = YamlConfiguration.loadConfiguration(file);
    }
}