package com.sunrisenw.bookandstorage.utils;

import org.bukkit.entity.Player;

public class Inventory {

    public static boolean isFull(Player player){
        return player.getInventory().firstEmpty() == -1;
    }

    public static int getAvailableSpace(Player player){
        return player.getInventory().getSize();
    }

}