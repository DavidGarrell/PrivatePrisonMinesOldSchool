package skyblock.api;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import skyblock.main.Main;

import java.util.*;

public class IslandManager {

    private static IslandManager instance;

    public Map<UUID, Mine> island = new HashMap<>();

    public static void setInstance(IslandManager instance) {
        IslandManager.instance = instance;
    }

    public Map<Mine, Location> islandLocationMap = new HashMap<>();

    public Map<Mine, Location> islandPlayerSpawnLocation = new HashMap<>();

    public Location lastIslandLocation = new Location(Main.emptyWorld, 0, 150, 0);

    public void IslandManager() {

    }
}
