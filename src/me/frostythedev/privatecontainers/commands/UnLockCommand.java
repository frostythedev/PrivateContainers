package me.frostythedev.privatecontainers.commands;

import me.frostythedev.privatecontainers.PCPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnLockCommand implements CommandExecutor {

    private PCPlugin plugin;

    public UnLockCommand(PCPlugin plugin) {
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
            plugin.getSetupManager().getUnlocking().add(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "You have entered setup mode. Please " + ChatColor.AQUA + "" +
                    "left-click" + ChatColor.GREEN + " a valid container to" + ChatColor.RED + " UNLOCK" +
                    ChatColor.GREEN + " it.");
        }
        return false;
    }
}
