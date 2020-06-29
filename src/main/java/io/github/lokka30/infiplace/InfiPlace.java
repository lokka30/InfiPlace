package io.github.lokka30.infiplace;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InfiPlace extends JavaPlugin implements Listener {

    ArrayList<Player> infinitePlacers = new ArrayList<>();

    @Override
    public void onEnable() {
        Bukkit.getLogger().info(prefix("InfiPlace:", "&8+----+&f (Enable Started) &8+----+"));
        final long startingTime = System.currentTimeMillis();

        Bukkit.getLogger().info(prefix("InfiPlace:", "&8(&3Startup &8- &31&8/&31&8) &7Registering events..."));
        getServer().getPluginManager().registerEvents(this, this);

        final long duration = System.currentTimeMillis() - startingTime;
        Bukkit.getLogger().info(prefix("InfiPlace:", "&8+----+&f (Enable Complete, took &b" + duration + "ms&f) &8+----+"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, @NotNull String[] args) {
        if(cmd.getLabel().equalsIgnoreCase("infiplace")) {
            if(sender instanceof Player) {
                if(sender.hasPermission("infiplace")) {
                    if(args.length == 0) {
                        Player player = (Player) sender;
                        if(infinitePlacers.contains(player)) {
                            infinitePlacers.remove(player);
                            player.sendMessage(prefix("InfiPlace:", "You have &cdisabled&7 InfiPlace."));
                        } else {
                            infinitePlacers.add(player);
                            player.sendMessage(prefix("InfiPlace:", "You have &aenabled&7 InfiPlace."));
                        }
                    } else {
                        sender.sendMessage(prefix("InfiPlace:", "Usage: &b/" + label));
                    }
                } else {
                    sender.sendMessage(prefix("InfiPlace:", "You don't have access to that."));
                }
            } else {
                sender.sendMessage(prefix("InfiPlace:", "Only players may access this command."));
            }
            return true;
        }
        return false;
    }

    public String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public String prefix(String prefix, String msg) {
        return colorize("&b&l" + prefix + "&7 " + msg);
    }

    @EventHandler
    public void onPlace(final BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if(infinitePlacers.contains(player) && !event.isCancelled()) {
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack offHand = player.getInventory().getItemInOffHand();

            if(mainHand.getType() != Material.AIR && mainHand.getType().isBlock()) {
                player.getInventory().setItemInMainHand(mainHand);
            } else if(offHand.getType() != Material.AIR && offHand.getType().isBlock()) {
                player.getInventory().setItemInOffHand(offHand);
            }
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        infinitePlacers.remove(event.getPlayer());
    }


}
