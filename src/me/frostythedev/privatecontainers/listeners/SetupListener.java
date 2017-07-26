package me.frostythedev.privatecontainers.listeners;

import com.google.common.collect.Lists;
import me.frostythedev.privatecontainers.PCPlugin;
import me.frostythedev.privatecontainers.containers.ContainerType;
import me.frostythedev.privatecontainers.containers.PContainer;
import me.frostythedev.privatecontainers.containers.Container;
import me.frostythedev.privatecontainers.containers.SpecialContainer;
import me.frostythedev.privatecontainers.utils.LocationUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;

import java.util.UUID;

public class SetupListener implements Listener {

    private PCPlugin plugin;

    public SetupListener(PCPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (plugin.getSetupManager().isSettingUp(uuid)) {
            if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                Block block = event.getClickedBlock();

                if (plugin.getSetupManager().isUnlocking(uuid)) {
                    if (plugin.getContainerManager().isContainer(block.getLocation())) {

                        if (plugin.getContainerManager().getContainer(block.getLocation()) instanceof Container) {
                            Container container = (Container) plugin.getContainerManager().getContainer(block.getLocation());

                            if (container.isLocked() && container.getOwner().equals(uuid)) {
                                if (plugin.getContainerStorage().remove(container)) {
                                    container.setLocked(false);
                                    container.setOwner(null);

                                    plugin.getContainerManager().getContainers().remove(container.getLocation());


                                    player.sendMessage(ChatColor.YELLOW + "You have UNLOCKED this container. Access Level: " +
                                            ChatColor.RED + "EVERYONE");

                                    plugin.getSetupManager().getUnlocking().remove(uuid);
                                } else {
                                    player.sendMessage(ChatColor.RED + "An error occurred, please try again.");
                                }

                            } else {
                                player.sendMessage(ChatColor.RED + "You do not have access to complete this action.");
                            }
                        } else if (plugin.getContainerManager().getContainer(block.getLocation()) instanceof SpecialContainer) {

                            SpecialContainer container = (SpecialContainer) plugin.getContainerManager().getContainer(block.getLocation());

                            if (container.isLocked() && container.getOwner().equals(uuid)) {
                                if (plugin.getSpecialContainerStorage().remove(container)) {
                                    container.setLocked(false);
                                    container.setOwner(null);

                                    container.getLocations().forEach(plugin.getContainerManager().getContainers()::remove);

                                    player.sendMessage(ChatColor.YELLOW + "You have UNLOCKED this container. Access Level: " +
                                            ChatColor.RED + "EVERYONE");

                                    plugin.getSetupManager().getUnlocking().remove(uuid);
                                } else {
                                    player.sendMessage(ChatColor.RED + "An error occurred, please try again.");
                                }

                            } else {
                                player.sendMessage(ChatColor.RED + "You do not have access to complete this action.");
                            }
                        }
                    }
                } else if (plugin.getSetupManager().isLocking(uuid)) {

                    if (plugin.getContainerManager().isContainer(block.getLocation())) {
                        player.sendMessage(ChatColor.RED + "This container is already locked.");
                    } else {
                        if (block.getType().equals(Material.CHEST)
                                || block.getType().equals(Material.FURNACE)
                                || block.getType().equals(Material.WOODEN_DOOR)
                                || block.getType().equals(Material.SPRUCE_DOOR)
                                || block.getType().equals(Material.BIRCH_DOOR)
                                || block.getType().equals(Material.JUNGLE_DOOR)
                                || block.getType().equals(Material.ACACIA_DOOR)
                                || block.getType().equals(Material.DARK_OAK_DOOR)
                                ) {

                            Container container = new Container(uuid, true, null, block.getLocation());
                            container.setRandomId(UUID.randomUUID());

                            switch (block.getType()) {
                                case CHEST:
                                    container.setContainerType(ContainerType.CHEST);
                                    Chest chest = (Chest) block.getState();
                                    if (chest.getInventory() instanceof DoubleChestInventory) {
                                        container = null;

                                        SpecialContainer newContainer = new SpecialContainer(uuid, true, ContainerType.DOUBLE_CHEST, null);
                                        newContainer.setRandomId(UUID.randomUUID());

                                        Location otherChest = LocationUtils.getRelativeOfSameTypeAround(block);
                                       if(otherChest != null){
                                           newContainer.setLocations(Lists.newArrayList(chest.getLocation(), otherChest));

                                           if (plugin.getContainerManager().addContainer(newContainer)) {
                                               player.sendMessage(ChatColor.YELLOW + "You have LOCKED this container. Access Level: " +
                                                       ChatColor.GREEN + "YOU ONLY");
                                               plugin.getSetupManager().getLocking().remove(uuid);
                                           } else {
                                               player.sendMessage(ChatColor.RED + "An error occurred, please try again.");
                                           }
                                       }else{
                                           plugin.getLogger().info("An error occurred while trying to find a valid block of that type.");
                                       }
                                    }
                                    break;
                                case FURNACE:
                                    container.setContainerType(ContainerType.NORMAL_FURNACE);
                                    break;
                                case BURNING_FURNACE:
                                    container.setContainerType(ContainerType.BURNING_FURNACE);
                                    break;
                                case WOODEN_DOOR:
                                case SPRUCE_DOOR:
                                case BIRCH_DOOR:
                                case JUNGLE_DOOR:
                                case ACACIA_DOOR:
                                case DARK_OAK_DOOR:
                                    container = null;
                                    SpecialContainer newContainer = new SpecialContainer(uuid, true, ContainerType.ENTIRE_DOOR, null);
                                    newContainer.setRandomId(UUID.randomUUID());

                                    Location otherDoor = LocationUtils.getRelativeOfSameType(block, BlockFace.UP, BlockFace.DOWN);
                                    if(otherDoor != null){
                                        newContainer.setLocations(Lists.newArrayList(block.getLocation(), otherDoor));

                                        if (plugin.getContainerManager().addContainer(newContainer)) {
                                            player.sendMessage(ChatColor.YELLOW + "You have LOCKED this container. Access Level: " +
                                                    ChatColor.GREEN + "YOU ONLY");
                                            plugin.getSetupManager().getLocking().remove(uuid);
                                        } else {
                                            player.sendMessage(ChatColor.RED + "An error occurred, please try again.");
                                        }
                                    }else{
                                        plugin.getLogger().info("An error occurred while trying to find a valid block of that type.");
                                    }
                                    break;
                            }

                            if (container != null) {
                                if (plugin.getContainerManager().addContainer(container)) {
                                    player.sendMessage(ChatColor.YELLOW + "You have LOCKED this container. Access Level: " +
                                            ChatColor.GREEN + "YOU ONLY");
                                    plugin.getSetupManager().getLocking().remove(uuid);
                                } else {
                                    player.sendMessage(ChatColor.RED + "An error occurred, please try again.");
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Invalid container type, please try again.");
                        }
                    }
                }
            }
        }
    }
}
