package me.frostythedev.privatecontainers.listeners;

import me.frostythedev.privatecontainers.PCPlugin;
import me.frostythedev.privatecontainers.containers.Container;
import me.frostythedev.privatecontainers.containers.PContainer;
import me.frostythedev.privatecontainers.core.PCConstants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ContainerOpenListener implements Listener {

    private PCPlugin plugin;

    public ContainerOpenListener(PCPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block clickedBlock = event.getClickedBlock();

            if (plugin.getContainerManager().isContainer(clickedBlock.getLocation())) {
                PContainer container = plugin.getContainerManager().getContainer(clickedBlock.getLocation());

                if (container.isLocked() && !container.getOwner().equals(event.getPlayer().getUniqueId())) {
                    if (event.getPlayer().hasPermission(PCConstants.BYPASS_PERMISSION)) {
                        event.setUseInteractedBlock(Event.Result.ALLOW);
                    } else {
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(ChatColor.RED + "You do not have access to open this container owned by: " + (Bukkit.getOfflinePlayer(container.getOwner()).getName()));
                    }
                }
            }
        }
    }
}
