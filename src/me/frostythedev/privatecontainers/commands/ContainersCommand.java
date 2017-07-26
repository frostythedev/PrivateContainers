package me.frostythedev.privatecontainers.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ContainersCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        sender.sendMessage(ChatColor.GOLD + "=====[ PrivateContainers Help " + ChatColor.AQUA + "Page 1/1 " + ChatColor.GOLD + "]======");
        sender.sendMessage(ChatColor.YELLOW + "/pcontainers" + ChatColor.GRAY + " - Shows this help page");
        sender.sendMessage(ChatColor.YELLOW + "/lock" + ChatColor.GRAY + " - Locks a container under your ownership");
        sender.sendMessage(ChatColor.YELLOW + "/unlock" + ChatColor.GRAY + " - Unlocks a container under your ownership");
        return false;
    }
}
