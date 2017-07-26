package me.frostythedev.privatecontainers.storage.types;

import me.frostythedev.privatecontainers.PCPlugin;
import me.frostythedev.privatecontainers.containers.Container;
import me.frostythedev.privatecontainers.containers.SpecialContainer;
import me.frostythedev.privatecontainers.storage.DataStorage;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class SpecialContainerFileStorage implements DataStorage<SpecialContainer> {

    private PCPlugin plugin;
    private File saveFile;
    private FileConfiguration config;

    public SpecialContainerFileStorage(PCPlugin plugin) {
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

        if (plugin.getConfig().getString("data.yml.file-special") != null) {
            this.saveFile = new File(plugin.getDataFolder(), plugin.getConfig().getString("data.yml.file-special"));
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
    public SpecialContainer load(String name) {
        if (this.config.getString(name) != null) {
            return plugin.getGson().fromJson(this.config.getString(name), SpecialContainer.class);
        }
        return null;
    }

    @Override
    public void save(SpecialContainer container) {
        if (container != null) {
            if(container.getRandomId() == null) container.setRandomId(UUID.randomUUID());
            this.config.set(container.getRandomId().toString(), plugin.getGson().toJson(container, SpecialContainer.class));
            try {
                this.config.save(this.saveFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean remove(SpecialContainer container) {
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
