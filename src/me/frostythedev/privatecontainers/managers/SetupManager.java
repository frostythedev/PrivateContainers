package me.frostythedev.privatecontainers.managers;

import com.google.common.collect.Sets;
import me.frostythedev.privatecontainers.PCPlugin;

import java.util.Set;
import java.util.UUID;

public class SetupManager {

    private Set<UUID> locking;
    private Set<UUID> unlocking;

    public SetupManager() {

        this.locking = Sets.newHashSet();
        this.unlocking = Sets.newHashSet();
    }

    public boolean isLocking(UUID uuid) {
        return locking.contains(uuid);
    }

    public boolean isUnlocking(UUID uuid) {
        return unlocking.contains(uuid);
    }

    public boolean isSettingUp(UUID uuid) {
        return isLocking(uuid) || isUnlocking(uuid);
    }

    public Set<UUID> getLocking() {
        return locking;
    }

    public Set<UUID> getUnlocking() {
        return unlocking;
    }
}
