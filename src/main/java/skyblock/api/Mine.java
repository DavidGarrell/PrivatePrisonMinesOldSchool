package skyblock.api;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import skyblock.main.Main;

import java.util.*;
import java.util.List;

public class Mine {

    private int ISLAND_SIZE = 30;
    private int ISLAND_HIGH = 40;
    private int ISLAND_LEVEL = 1;

    public static final int ISLAND_LEVEL_MAX = 1000;
    public static final int ISLAND_START_SIZE = 30;
    public static final int ISLAND_START_HIGH = 40;
    public static Plugin plugin;
    private Location islandLocation;

    private Player islandOwner;

    private String prefix = Main.prefix;
    private IslandManager islandManager = Main.islandManager;


    public Mine(Player owner, Plugin plugin) {
        this.islandOwner = owner;
        Mine.plugin = plugin;
    }

    public void createIsland(Player player) {
        if (islandManager.island.containsKey(player.getUniqueId())) {
            player.sendMessage(prefix + "§cYou already have an island!");
            return;
        }
        IslandGenerator islandGenerator = new IslandGenerator();

        this.islandLocation = getNextGridLocation(islandManager.lastIslandLocation);
        islandGenerator.loadIslandSchematic(player, "/island.schem", islandLocation);

        islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE+1, ISLAND_SIZE+1, ISLAND_START_HIGH, player);
        islandGenerator.generateIsland(islandLocation, ISLAND_START_SIZE, ISLAND_START_SIZE, ISLAND_START_HIGH, 0, player);
        islandGenerator.generateBedrockFrame(islandLocation, ISLAND_START_SIZE + 4, ISLAND_START_SIZE + 4, ISLAND_START_HIGH, player);
        islandGenerator.clearIslandSchem(getIslandLocation(), ISLAND_SIZE+4, ISLAND_SIZE+4, ISLAND_HIGH+1, player);
        mineAutoResetByPercent(player);
        islandManager.lastIslandLocation = islandLocation;

    }



    private Location getNextGridLocation(final Location lastIsland) {
        int x = lastIsland.getBlockX();
        int z = lastIsland.getBlockZ();
        int d = 500;

        Location nextIsland = lastIsland.clone();

        if (x < z) {
            if (-1 * x < z) {
                nextIsland.setX(nextIsland.getX() + d);
                return nextIsland;
            }
            nextIsland.setZ(nextIsland.getZ() + d);
            return nextIsland;
        }
        if (x > z) {
            if (-1 * x >= z) {
                nextIsland.setX(nextIsland.getX() - d);
                return nextIsland;
            }
            nextIsland.setZ(nextIsland.getZ() - d);
            return nextIsland;
        }
        if (x <= 0) {
            nextIsland.setZ(nextIsland.getZ() + d);
            return nextIsland;
        }
        nextIsland.setZ(nextIsland.getZ() - d);
        return nextIsland;
    }
    
    public void islandUpgrade(Player player) {

        if(getISLAND_LEVEL()<ISLAND_LEVEL_MAX) {
            this.ISLAND_LEVEL+=1;

            int next_block = ISLAND_LEVEL/10;
            int next_reward_level = 10-ISLAND_LEVEL%10;

            if (this.ISLAND_LEVEL % 10 == 0) {
                IslandGenerator islandGenerator = new IslandGenerator();
                String color = RewardStrings.getRandomColor()+ "§l";
                player.sendMessage(color + RewardStrings.getRandomMessage() + "§fYou have reached mine level " + color + ISLAND_LEVEL);
                IslandMaterials islandMaterials = new IslandMaterials();
                player.sendMessage("§6§lUNLOCK §fnew Mine Block: §f" + islandMaterials.islandMaterials.get(next_block));

                islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, player);
                islandGenerator.clearIslandFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player);

                this.ISLAND_SIZE = ISLAND_START_SIZE + ISLAND_LEVEL/10 * 2;
                this.ISLAND_HIGH = ISLAND_START_HIGH + ISLAND_LEVEL/10;

                islandGenerator.clearIslandSchem(getIslandLocation(), ISLAND_SIZE+4, ISLAND_SIZE+4, ISLAND_HIGH+1, player);
                islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE+1, ISLAND_SIZE+1, ISLAND_HIGH+1, player);
                islandGenerator.generateIsland(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player);
                islandGenerator.generateBedrockFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player);

            }
            else {
                String color = RewardStrings.getRandomColor()+ "§l";
                player.sendMessage(color + RewardStrings.getRandomMessage() + "§fYou have reached mine level " + color + ISLAND_LEVEL + ". §fYou need " + color + next_reward_level + " §fmore mine level to get a new mine block");
            }
        }

    }

    public Location getIslandLocation() {
        return islandLocation;
    }

    public int getISLAND_LEVEL() {
        return ISLAND_LEVEL;
    }


    public void setISLAND_LEVEL(int ISLAND_LEVEL, Player player) {

        IslandGenerator islandGenerator = new IslandGenerator();

        int ISLAND_LEVEL_BEFORE = this.ISLAND_LEVEL;
        this.ISLAND_LEVEL = ISLAND_LEVEL;

        if (this.ISLAND_LEVEL % 10 == 0) {

            islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, player);
            islandGenerator.clearIslandFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player);

            this.ISLAND_SIZE = ISLAND_START_SIZE + ISLAND_LEVEL/10 * 2;
            this.ISLAND_HIGH = ISLAND_START_HIGH + ISLAND_LEVEL/10;

            if(ISLAND_LEVEL<ISLAND_LEVEL_BEFORE) {
                islandGenerator.loadIslandSchematic(player, "/island.schem", islandLocation);
            }
            islandGenerator.clearIslandSchem(getIslandLocation(), ISLAND_SIZE+4, ISLAND_SIZE+4, ISLAND_HIGH+1, player);
            islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE+1, ISLAND_SIZE+1, ISLAND_HIGH+1, player);
            islandGenerator.generateIsland(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player);
            islandGenerator.generateBedrockFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player);


        }
    }

    public Location getIslandPlayerSpawnPoint() {

        Location playerSpawnLoc = islandLocation.clone();

        playerSpawnLoc.add(0.5, 0, -ISLAND_SIZE/2-3.5);

        return playerSpawnLoc;
    }

    public void resetIsland(Player player) {
        IslandGenerator islandGenerator = new IslandGenerator();
        islandGenerator.resetIsland(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player);

    }
    public Player getIslandOwner() {
        return islandOwner;
    }

    public void setIslandOwner(Player islandOwner) {
        this.islandOwner = islandOwner;
    }

    private int taskid_p = -2;
    public void mineAutoResetByPercent(Player player) {

        IslandGenerator islandGenerator = new IslandGenerator();

        taskid_p = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            if(islandGenerator.getBlocksInMine(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player)/2 <=islandGenerator.getAirBlocksInMine(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player)) {
                resetIsland(player);
            } else if (!player.isOnline()) {
                Bukkit.getServer().getScheduler().runTask(plugin, () -> Bukkit.getServer().getScheduler().cancelTask(taskid_p));
            }
        }, 0, 60);
    }


    public boolean checkIfBlockIsInMine(Location location) {

        Location islandLocation = getIslandLocation();
        int startX = islandLocation.getBlockX() - ISLAND_SIZE / 2;
        int startY = islandLocation.getBlockY() - ISLAND_HIGH;
        int startZ = islandLocation.getBlockZ() - ISLAND_SIZE / 2;

        int endX = startX + ISLAND_SIZE;
        int endY = startY + ISLAND_HIGH-1;
        int endZ = startZ + ISLAND_SIZE;

        int blockX = location.getBlockX();
        int blockY = location.getBlockY();
        int blockZ = location.getBlockZ();

        return blockX >= startX && blockX <= endX
                && blockY >= startY && blockY <= endY
                && blockZ >= startZ && blockZ <= endZ;
    }

    public int getISLAND_SIZE() {
        return ISLAND_SIZE;
    }
}
