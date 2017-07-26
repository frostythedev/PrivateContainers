package me.frostythedev.privatecontainers.storage.types;

import me.frostythedev.privatecontainers.PCPlugin;
import me.frostythedev.privatecontainers.containers.Container;
import me.frostythedev.privatecontainers.containers.PContainer;
import me.frostythedev.privatecontainers.containers.SpecialContainer;
import me.frostythedev.privatecontainers.storage.DataStorage;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ContainerFileStorage implements DataStorage<Container> {

    private PCPlugin plugin;
    private File saveFile;
    private FileConfiguration config;

    public ContainerFileStorage(PCPlugin plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public void setup() {
        if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
            plugin.saveDefaultConfig();
        }

        if (plugin.getConfig().getString("data.yml.file") != null) {
            this.saveFile = new File(plugin.getDataFolder(), plugin.getConfig().getString("data.yml.file"));
            if (!saveFile.exists()) {
                try {
                    saveFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            config = new YamlConfiguration();
            try {
                config.load(saveFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Container load(String name) {
        if (this.config.getString(name) != null) {
            return plugin.getGson().fromJson(this.config.getString(name), Container.class);
        }
        return null;
    }

    @Override
    public void save(Container container) {
        if (container != null) {
            if(container.getRandomId() == null) container.setRandomId(UUID.randomUUID());
            this.config.set(container.getRandomId().toString(), plugin.getGson().toJson(container, Container.class));
            try {
                this.config.save(this.saveFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean remove(Container container) {
        if (this.config.getString(container.getRandomId().toString()) != null) {
            this.config.set(container.getRandomId().toString(), null);
            try {
                this.config.save(this.saveFile);
                return true;
            } catch (IOException ignored) {
                return false;
            }
        }else{
            return false;
        }
    }
}
