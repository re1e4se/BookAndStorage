package com.sunrisenw.bookandstorage.utils;

import org.bukkit.Bukkit;

public class Server {

    public static String getServerVersion(){
        return Bukkit.getVersion();
    }

    public static boolean isOld(){
        int ver = Integer.parseInt(getServerVersion().split(".")[1]);
        return ver < 14;
    }

}