package me.frostythedev.privatecontainers.threads;

import me.frostythedev.privatecontainers.PCPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ContainerSaver extends BukkitRunnable {

    protected PCPlugin plugin;

    public ContainerSaver(PCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getContainerManager().saveContainers();
    }
}
