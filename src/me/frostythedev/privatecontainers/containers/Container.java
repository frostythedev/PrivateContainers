package me.frostythedev.privatecontainers.containers;

import me.frostythedev.privatecontainers.containers.ContainerType;
import me.frostythedev.privatecontainers.containers.PContainer;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.UUID;

public class Container implements PContainer {

    private UUID randomId;

    private UUID owner;
    private boolean locked;
    private ContainerType containerType;
    private Location location;

    public Container(UUID owner, boolean locked, ContainerType containerType, Location location) {
        this.owner = owner;
        this.locked = locked;
        this.containerType = containerType;
        this.location = location;
    }

    @Override
    public UUID getRandomId() {
        return randomId;
    }

    @Override
    public void setRandomId(UUID randomId) {
        this.randomId = randomId;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public ContainerType getContainerType() {
        return containerType;
    }

    @Override
    public void setContainerType(ContainerType containerType) {
        this.containerType = containerType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
