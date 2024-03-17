package com.sunrisenw.bookandstorage.commands;

import com.sunrisenw.bookandstorage.Config;
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

        int bookPageSize = 100;

        if (!Config.get().getBoolean("splitBooks")) {
            if (chunks.length > bookPageSize){
                player.sendMessage("§cThe file you provided is too big to fit into a single book! If you want to store bigger files, change splitBooks option on config to true.");
                return true;
            }
            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);

            BookMeta meta = (BookMeta) book.getItemMeta();
            meta.setTitle(args[0]);
            meta.setAuthor("BookAndStorage");
            for (String chunk : chunks) {
                meta.addPage(chunk);
            }

            book.setItemMeta(meta);
            player.getInventory().addItem(book);
            return true;
        }

        List<String[]> bookChunks = chunkArray(chunks, bookPageSize);
        int part = 0;
        for (String[] chunk : bookChunks){
            if (Config.get().getBoolean("clearInventory"))
                player.getInventory().clear();

            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);

            BookMeta meta = (BookMeta)book.getItemMeta();
            meta.setTitle(args[0] + "." + part);
            meta.setAuthor("BookAndStorage");
            for (String c : bookChunks.get(part)){
                meta.addPage(c);
            }

            book.setItemMeta(meta);
            player.getInventory().addItem(book);
            part++;
        }

        return true;
    }

    public static List<String[]> chunkArray(String[] array, int chunkSize){
        List<String[]> chunkedArrays = new ArrayList<>();

        int numChunks = (array.length + chunkSize - 1) / chunkSize;

        for (int i = 0; i < numChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, array.length);
            String[] chunk = Arrays.copyOfRange(array, start, end);
            chunkedArrays.add(chunk);
        }

        return chunkedArrays;
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