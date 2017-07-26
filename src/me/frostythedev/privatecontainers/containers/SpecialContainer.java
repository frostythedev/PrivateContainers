package me.frostythedev.privatecontainers.containers;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public class SpecialContainer implements PContainer {

    private final ContainerType[] VALID_TYPES = new ContainerType[]{
            ContainerType.DOUBLE_CHEST, ContainerType.ENTIRE_DOOR};

    private UUID randomId;

    private UUID owner;
    private boolean locked;
    private ContainerType containerType;
    private List<Location> locations;

    public SpecialContainer(UUID owner, boolean locked, ContainerType containerType, List<Location> locations) {
        Validate.isTrue(VALID_TYPES[0].equals(containerType) || VALID_TYPES[1].equals(containerType));
        this.owner = owner;
        this.locked = locked;
        this.containerType = containerType;
        this.locations = locations;
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

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
