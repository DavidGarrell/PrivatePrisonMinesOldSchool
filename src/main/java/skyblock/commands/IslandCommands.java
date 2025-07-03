package skyblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import skyblock.api.IslandGenerator;
import skyblock.api.IslandManager;
import skyblock.api.Mine;
import skyblock.main.Main;


import java.util.UUID;

public class IslandCommands implements CommandExecutor {

    private Main plugin;

    private String prefix = Main.prefix;

    IslandManager islandManager = Main.islandManager;
    IslandGenerator islandGenerator = new IslandGenerator();
    public IslandCommands(Main plugin) {
        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            Mine island = islandManager.island.get(player.getUniqueId());

            if (label.equalsIgnoreCase("mine") || label.equalsIgnoreCase("pmine")) {
                if (args.length == 0) {

                    if (island == null) {
                        island = new Mine(player, plugin);
                        island.createIsland(player);
                        sender.sendMessage(prefix + "§fMine has been created");
                        player.teleport(island.getIslandLocation());

                        islandManager.island.put(player.getUniqueId(), island);
                    } else {
                        player.teleport(island.getIslandPlayerSpawnPoint());
                        sender.sendMessage(prefix + "teleporting to your Mine");
                    }
                } else if (args[0].equalsIgnoreCase("reset")) {
                    if (island == null) {
                        sender.sendMessage(prefix + "§fYou don't have a Mine /mine to create one");

                    } else {
                        player.teleport(island.getIslandLocation());
                        island.resetIsland((Player) sender);
                        sender.sendMessage(prefix + "§fMine has been reset");
                    }

                } else if (args[0].equalsIgnoreCase("upgrade")) {
                    if(sender.hasPermission("mine.upgrade") || sender.hasPermission("mine.admin")) {
                        Player targetPlayer = plugin.getServer().getPlayer(args[1]);

                        if (args.length < 2 || args[1].isEmpty()) {
                            player.sendMessage(prefix + "use /mine upgrade <targetPlayerName>");

                        } else {
                            island = islandManager.island.get(targetPlayer.getUniqueId());
                            if (targetPlayer != null && island != null) {

                                island.islandUpgrade(targetPlayer);
                            } else {

                                player.sendMessage(targetPlayer.getName() + " has no Mine");
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("setlevel")) {
                    if(sender.hasPermission("mine.setlevel") || sender.hasPermission("mine.admin")) {
                        Player targetPlayer = plugin.getServer().getPlayer(args[1]);
                        int level = Integer.parseInt(args[2]);

                        if (args.length < 3 || args[1].isEmpty()) {
                            player.sendMessage(prefix + "use /mine setlevel <targetPlayerName> <level>");

                        } else {
                            island = islandManager.island.get(targetPlayer.getUniqueId());
                            if (targetPlayer != null && island != null) {

                                island.setISLAND_LEVEL(level, targetPlayer);
                                player.sendMessage(prefix + "Mine level from " + targetPlayer.getName() + "'s Mine has changed to " + island.getISLAND_LEVEL());

                            } else {

                                assert targetPlayer != null;
                                player.sendMessage(targetPlayer.getName() + " has no Mine");
                            }
                        }
                    }

                } else if (args[0].equalsIgnoreCase("tp")) {
                    Player targetPlayer = plugin.getServer().getPlayer(args[1]);


                    player.teleport(islandManager.island.get(player.getUniqueId()).getIslandLocation());

                    if (args.length < 2 || args[1].isEmpty()) {

                    } else {
                        island = islandManager.island.get(targetPlayer.getUniqueId());
                        if (targetPlayer != null && island != null) {

                            player.teleport(islandManager.island.get(targetPlayer.getUniqueId()).getIslandLocation());

                            player.sendMessage(prefix + "teleporting to " + targetPlayer.getName() + "'s pmine");
                        } else {
                            player.sendMessage(prefix + (args[1]) + " has no Mine");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("help")) {

                    player.sendMessage("Mine Commands:");
                    player.sendMessage("- /mine");
                    player.sendMessage("- /mine menu");
                    player.sendMessage("- /mine tp <player>");
                    player.sendMessage("- /mine reset");

                }



                return false;
            }
            return false;
        }
        return false;
    }
}
