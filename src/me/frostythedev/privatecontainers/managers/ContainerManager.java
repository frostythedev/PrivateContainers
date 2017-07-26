package me.frostythedev.privatecontainers.managers;

import com.google.common.collect.Maps;
import me.frostythedev.privatecontainers.PCPlugin;
import me.frostythedev.privatecontainers.containers.PContainer;
import me.frostythedev.privatecontainers.containers.Container;
import me.frostythedev.privatecontainers.containers.SpecialContainer;
import me.frostythedev.privatecontainers.storage.types.ContainerFileStorage;
import me.frostythedev.privatecontainers.storage.types.ContainerSQLStorage;
import me.frostythedev.privatecontainers.storage.types.SpecialContainerFileStorage;
import me.frostythedev.privatecontainers.storage.types.SpecialContainerSQLStorage;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ContainerManager {

    private PCPlugin plugin;
    private Map<Location, Container> containers;
    private Map<Location, SpecialContainer> specialContainers;

    public ContainerManager(PCPlugin plugin) {
        this.plugin = plugin;

        this.containers = Maps.newHashMap();
        this.specialContainers = Maps.newHashMap();
    }

    public boolean isContainer(Location location) {
        return containers.containsKey(location) || specialContainers.containsKey(location);
    }

    public boolean isLoaded(PContainer container) {
        for (Container c : containers.values()) {
            if (c.getRandomId().equals(container.getRandomId())) {
                return true;
            }
        }
        for (SpecialContainer c : specialContainers.values()) {
            if (c.getRandomId().equals(container.getRandomId())) {
                return true;
            }
        }
        return false;
    }

    public PContainer getContainer(Location location) {
        if (isContainer(location)) {
            if (containers.containsKey(location)) {
                return containers.get(location);
            } else {
                return specialContainers.get(location);
            }
        }
        return null;
    }

    public boolean addContainer(PContainer container) {
        if (container instanceof Container) {
            Container normal = (Container) container;
            if (isContainer(normal.getLocation())) {
                return false;
            } else {
                containers.put(normal.getLocation(), normal);
                return true;
            }
        } else if (container instanceof SpecialContainer) {
            SpecialContainer special = (SpecialContainer) container;
            for (Location loc : special.getLocations()) {
                if (isContainer(loc)) {
                    return false;
                } else {
                    specialContainers.put(loc, special);
                }
            }
            return true;
        }
        return false;
    }

    public void loadContainers() {
        containers.clear();
        specialContainers.clear();

        if (plugin.getContainerStorage() instanceof ContainerFileStorage) {
            ContainerFileStorage cfs = (ContainerFileStorage) plugin.getContainerStorage();
            for (String keys : cfs.getConfig().getKeys(false)) {
                Container c = cfs.load(keys);
                if (c != null) {
                    this.addContainer(c);
                }
            }
        } else if (plugin.getContainerStorage() instanceof ContainerSQLStorage) {
            ContainerSQLStorage css = (ContainerSQLStorage) plugin.getContainerStorage();
            String query = "SELECT * FROM pc_table;";

            ResultSet rs = css.getMySQL().syncQuery(query);
            if (rs != null) {
                try {
                    if (rs.next()) {
                        do {
                            UUID randomId = UUID.fromString(String.valueOf(rs.getString("randomId")));
                            Container c = plugin.getGson().fromJson(rs.getString("data"), Container.class);
                            c.setRandomId(randomId);
                            this.addContainer(c);
                        } while (rs.next());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (plugin.getSpecialContainerStorage() instanceof SpecialContainerFileStorage) {
            SpecialContainerFileStorage cfs = (SpecialContainerFileStorage) plugin.getSpecialContainerStorage();
            for (String keys : cfs.getConfig().getKeys(false)) {
                SpecialContainer c = cfs.load(keys);
                if (c != null) {
                    this.addContainer(c);
                }
            }
        } else if (plugin.getSpecialContainerStorage() instanceof SpecialContainerSQLStorage) {
            SpecialContainerSQLStorage css = (SpecialContainerSQLStorage) plugin.getSpecialContainerStorage();

            ResultSet rs = css.getMySQL().syncQuery("SELECT * FROM pc_special_table;");
            if (rs != null) {
                try {
                    if (rs.next()) {
                        do {
                            UUID randomId = UUID.fromString(String.valueOf(rs.getString("randomId")));
                            SpecialContainer c = plugin.getGson().fromJson(rs.getString("data"), SpecialContainer.class);
                            c.setRandomId(randomId);
                            this.addContainer(c);
                        } while (rs.next());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void saveContainers() {
        if (plugin.getContainerStorage() != null) {
            if (!containers.isEmpty()) {
                containers.values().forEach(plugin.getContainerStorage()::save);
            }
        }
        if (plugin.getSpecialContainerStorage() != null) {
            if (!specialContainers.isEmpty()) {
                specialContainers.values().forEach(plugin.getSpecialContainerStorage()::save);
            }
        }
    }

    public Map<Location, Container> getContainers() {
        return containers;
    }

    public Map<Location, SpecialContainer> getSpecialContainers() {
        return specialContainers;
    }
}
