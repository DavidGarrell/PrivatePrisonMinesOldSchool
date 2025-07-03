package skyblock.main;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import skyblock.api.IslandManager;
import skyblock.api.IslandMaterials;
import skyblock.api.WorldManager;
import skyblock.commands.IslandCommands;
import skyblock.events.PlayerJoin;
import skyblock.listeners.Listeners;
import skyblock.store.PlayerStore;

import java.io.File;
import java.util.*;

public final class Main extends JavaPlugin {

    public static Main instance;
    public static World emptyWorld;

    public static IslandManager islandManager;
    public static IslandMaterials islandMaterials = new IslandMaterials();

    public static String prefix = "§bMines | ";

    private static File dataFolder;
    private static FileConfiguration configuration;

    public PlayerStore playerStore;




    @Override
    public void onEnable() {



        dataFolder = new File(getDataFolder().getPath());
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        emptyWorld = WorldManager.createEmptyWorld("Mines");
        instance = this;
        islandManager = new IslandManager();
        playerStore = new PlayerStore();

        Objects.requireNonNull(getCommand("mine")).setExecutor(new IslandCommands(this));
        Objects.requireNonNull(getCommand("pmine")).setExecutor(new IslandCommands(this));

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Listeners(), this);

    }


    @Override
    public void onDisable() {


    }

}
