package me.frostythedev.privatecontainers.containers;

import org.bukkit.Location;

import java.util.UUID;

public interface PContainer {

    UUID getRandomId();
    void setRandomId(UUID randomId);
    UUID getOwner();
    void setOwner(UUID uuid);
    boolean isLocked();
    void setLocked(boolean locked);
    ContainerType getContainerType();
    void setContainerType(ContainerType type);
}
