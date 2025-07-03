package skyblock.events;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import skyblock.api.IslandManager;
import skyblock.api.Mine;
import skyblock.main.Main;
import skyblock.store.PlayerStore;

public class PlayerJoin implements Listener {

    private Main plugin;

    IslandManager islandManager = Main.islandManager;
    private String prefix = Main.prefix;
    public PlayerJoin(Main plugin) {
        this.plugin = plugin;

    }


    @EventHandler

    public void onJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();
        PlayerStore playerStore = Main.instance.playerStore;

        playerStore.addJoined_Player(player);

        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(true);
        player.setFlying(true);

        if(Main.islandManager.island.get(e.getPlayer().getUniqueId())==null) {

            Mine island = new Mine(player, plugin);
            island.createIsland(player);
            islandManager.island.put(player.getUniqueId(), island);
            player.teleport(island.getIslandPlayerSpawnPoint());
        } else {
            Mine island = islandManager.island.get(player.getUniqueId());
            player.teleport(island.getIslandPlayerSpawnPoint());
        }

    }
}
