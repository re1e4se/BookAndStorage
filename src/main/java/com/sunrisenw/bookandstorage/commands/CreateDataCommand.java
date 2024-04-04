package com.sunrisenw.bookandstorage.commands;

import com.sunrisenw.bookandstorage.Config;
import com.sunrisenw.bookandstorage.utils.Inventory;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateDataCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }
        Player player = (Player)sender;
        if (args.length != 1){
            player.sendMessage("§cUsage: /createdata <fileName>");
            return true;
        }
        File file = Config.getFileInData(args[0]);
        String absolutePath = file.getAbsolutePath().replace("\\", "/");
        if (!file.exists()){
            player.sendMessage("§cFile not found!");
            return true;
        }
        if (!absolutePath.contains(Config.getPluginDataPath())){
            player.sendMessage("§cYou can't access files outside of data folder!");
            System.out.println("[BookAndStorage] Player " + player.getName() + " tried to access files outside of data folder!");
            return true;
        }

        StringBuilder hexBuilder = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];

            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    hexBuilder.append(String.format("%02X ", buffer[i]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String hexString = hexBuilder.toString();
        String[] chunks = splitStringIntoChunks(hexString, 256);

        int bookPageSize = Config.get().getInt("bookPageLimit");

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor("BookAndStorage");

        if (!Config.get().getBoolean("splitBooks")) {
            if (chunks.length > bookPageSize){
                player.sendMessage("§cThe file you provided is too big to fit into a single book! If you want to store bigger files, change splitBooks option on config to true.");
                return true;
            }
            meta.setTitle(args[0]);
            for (String chunk : chunks) {
                meta.addPage(chunk);
            }

            book.setItemMeta(meta);
            player.getInventory().addItem(book);
            return true;
        }

        List<String[]> bookChunks = chunkArray(chunks, bookPageSize);
        int part = 0;
        int inventorySize = Config.get().getInt("inventorySize");
        if (!Config.get().getBoolean("useShulkers")) {
            if (bookChunks.size() > inventorySize){
                player.sendMessage("§cThe file you provided can't be fit into your inventory! If you want to store bigger files, change useShulkers option to true or increase the inventorySize on config.");
                return true;
            }
            for (String[] chunk : bookChunks) {
                if (Config.get().getBoolean("clearInventory") && Inventory.getAvailableSpace(player) > bookChunks.size())
                    player.getInventory().clear();

                if (Inventory.getAvailableSpace(player) > bookChunks.size()){
                    player.sendMessage("§cYou don't have enough space in your inventory to receive this file! Clear your inventory.");
                    return true;
                }

                meta.setTitle(args[0] + "." + part);

                for (String c : bookChunks.get(part)) {
                    meta.addPage(c);
                }

                book.setItemMeta(meta);
                player.getInventory().addItem(book);
                part++;
            }

            return true;
        }

        return true;
    }

    private static <T> List<T[]> chunkArray(T[] array, int chunkSize) {
        List<T[]> chunkedArrays = new ArrayList<>();

        int numChunks = (array.length + chunkSize - 1) / chunkSize;

        for (int i = 0; i < numChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, array.length);
            T[] chunk = Arrays.copyOfRange(array, start, end);
            chunkedArrays.add(chunk);
        }

        return chunkedArrays;
    }

    // This will be soon used for chunking books to shulkers.
    private static <T> List<List<T[]>> chunkList(List<T[]> originalList, int chunkSize) {
        List<List<T[]>> chunkedLists = new ArrayList<>();

        for (int i = 0; i < originalList.size(); i += chunkSize) {
            int endIndex = Math.min(i + chunkSize, originalList.size());
            chunkedLists.add(originalList.subList(i, endIndex));
        }

        return chunkedLists;
    }

    private static String[] splitStringIntoChunks(String str, int chunkSize) {
        int len = str.length();
        int numOfChunks = (len + chunkSize - 1) / chunkSize;
        String[] chunks = new String[numOfChunks];
        for (int i = 0; i < numOfChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, len);
            chunks[i] = str.substring(start, end);
        }
        return chunks;
    }
}