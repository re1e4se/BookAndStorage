package com.sunrisenw.bookandstorage.commands;

import com.sunrisenw.bookandstorage.Config;
import com.sunrisenw.bookandstorage.utils.HexDecoder;
import com.sunrisenw.bookandstorage.utils.HexValidator;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteDataCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }
        Player player = (Player)sender;
        if (args.length != 1){
            player.sendMessage("§cUsage: /writedata <fileName>");
            return true;
        }

        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType() == Material.WRITABLE_BOOK || heldItem.getType() == Material.WRITTEN_BOOK){
            BookMeta meta = (BookMeta)heldItem.getItemMeta();
            String data = "";
            StringBuilder stringBuilder = new StringBuilder();
            for (String page : meta.getPages()){
                String hex = page.replace(" ", "");
                if ((!HexValidator.isValidHex(hex))){
                    player.sendMessage("§cOne of the book's pages is not a valid hexadecimal string!");
                    return true;
                }
                stringBuilder.append(hex);
            }

            data = stringBuilder.toString();
            File file = Config.getFileInData(args[0]);
            String filePath = file.getPath();
            String absolutePath = file.getAbsolutePath().replace("\\", "/");
            if (!absolutePath.contains(Config.getPluginDataPath())){
                player.sendMessage("§cYou can't access files outside of data folder!");
                System.out.println("[BookAndStorage] Player " + player.getName() + " tried to access files outside of data folder!");
                return true;
            }
            if (!file.exists()){
                try{
                    file.createNewFile();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            String actualString = new String(HexDecoder.decode(data));

            try(PrintWriter writer = new PrintWriter(filePath)){
                writer.println(actualString);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        return true;
    }
}