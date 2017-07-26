package me.frostythedev.privatecontainers.commands;

import me.frostythedev.privatecontainers.PCPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LockCommand implements CommandExecutor {

    private PCPlugin plugin;

    public LockCommand(PCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to execute this command.");
        }
        Player player = (Player) sender;
        if (plugin.getSetupManager().isSettingUp(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You cannot complete this action until you have finished setting " +
                    "up your last container.");
        } else {
            plugin.getSetupManager().getLocking().add(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "You have entered setup mode. Please " + ChatColor.AQUA + "" +
                    "left-click" + ChatColor.GREEN + " a valid container to" + ChatColor.RED + " LOCK" +
                    ChatColor.GREEN + " it.");
        }
        return false;
    }
}
