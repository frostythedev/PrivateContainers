package me.frostythedev.privatecontainers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.frostythedev.privatecontainers.adaptors.ContainerAdaptor;
import me.frostythedev.privatecontainers.adaptors.SpecialContainerAdaptor;
import me.frostythedev.privatecontainers.commands.ContainersCommand;
import me.frostythedev.privatecontainers.commands.LockCommand;
import me.frostythedev.privatecontainers.commands.UnLockCommand;
import me.frostythedev.privatecontainers.containers.Container;
import me.frostythedev.privatecontainers.containers.PContainer;
import me.frostythedev.privatecontainers.containers.SpecialContainer;
import me.frostythedev.privatecontainers.listeners.ContainerOpenListener;
import me.frostythedev.privatecontainers.listeners.SetupListener;
import me.frostythedev.privatecontainers.managers.ContainerManager;
import me.frostythedev.privatecontainers.managers.SetupManager;
import me.frostythedev.privatecontainers.storage.DataStorage;
import me.frostythedev.privatecontainers.storage.types.ContainerFileStorage;
import me.frostythedev.privatecontainers.storage.types.ContainerSQLStorage;
import me.frostythedev.privatecontainers.storage.types.SpecialContainerFileStorage;
import me.frostythedev.privatecontainers.storage.types.SpecialContainerSQLStorage;
import me.frostythedev.privatecontainers.threads.ContainerSaver;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PCPlugin extends JavaPlugin {

    private DataStorage<Container> containerStorage;
    private DataStorage<SpecialContainer> specialContainerStorage;

    private ContainerManager containerManager;
    private SetupManager setupManager;

    private Gson gson;

    @Override
    public void onEnable() {
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
        }

        if (getConfig().getString("data.type").equalsIgnoreCase("YML")) {
            this.setContainerStorage(new ContainerFileStorage(this));
            this.setSpecialContainerStorage(new SpecialContainerFileStorage(this));
            this.getLogger().info("Initialized using data storage type: YML");

        } else if (getConfig().getString("data.type").equalsIgnoreCase("SQL")) {
            this.setContainerStorage(new ContainerSQLStorage(this));
            this.setSpecialContainerStorage(new SpecialContainerSQLStorage(this));
            this.getLogger().info("Initialized using data storage type: SQL");

        } else {
            this.getConfig().set("data.type", "YML");
            this.saveConfig();
            this.setContainerStorage(new ContainerFileStorage(this));
            this.setSpecialContainerStorage(new SpecialContainerFileStorage(this));
            this.getLogger().info("Initialized using default data storage type: YML");
        }

        this.containerStorage.setup();
        this.specialContainerStorage.setup();

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(Container.class, new ContainerAdaptor());
        builder.registerTypeAdapter(SpecialContainer.class, new SpecialContainerAdaptor());
        this.gson = builder.create();

        this.containerManager = new ContainerManager(this);
        this.setupManager = new SetupManager();

        this.getServer().getPluginManager().registerEvents(new ContainerOpenListener(this), this);
        this.getServer().getPluginManager().registerEvents(new SetupListener(this), this);

        this.getServer().getPluginCommand("pcontainers").setExecutor(new ContainersCommand());
        this.getServer().getPluginCommand("lock").setExecutor(new LockCommand(this));
        this.getServer().getPluginCommand("unlock").setExecutor(new UnLockCommand(this));

        this.getContainerManager().loadContainers();

        int syncTime = getConfig().getInt("data.sync-time");
        if(syncTime == 0) syncTime = 20*15;

        ContainerSaver saver = new ContainerSaver(this);
        saver.runTaskTimerAsynchronously(this, syncTime, syncTime);
    }

    @Override
    public void onDisable(){
        this.getContainerManager().saveContainers();
    }
    public Gson getGson() {
        return gson;
    }

    public ContainerManager getContainerManager() {
        return containerManager;
    }

    public SetupManager getSetupManager() {
        return setupManager;
    }

    public DataStorage<Container> getContainerStorage() {
        return containerStorage;
    }

    public void setContainerStorage(DataStorage<Container> containerStorage) {
        this.containerStorage = containerStorage;
    }

    public DataStorage<SpecialContainer> getSpecialContainerStorage() {
        return specialContainerStorage;
    }

    public void setSpecialContainerStorage(DataStorage<SpecialContainer> specialContainerStorage) {
        this.specialContainerStorage = specialContainerStorage;
    }
}
