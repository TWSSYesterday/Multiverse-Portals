/**
 * (c) 2015 dmulloy2
 */
package com.onarandombox.MultiversePortals.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.NumberConversions;

import com.onarandombox.MultiversePortals.MVPortal;
import com.onarandombox.MultiversePortals.MultiversePortals;
import com.onarandombox.MultiversePortals.PortalLocation;

/**
 * @author dmulloy2
 */

public class NearbyCommand extends PortalCommand {

    public NearbyCommand(MultiversePortals plugin) {
        super(plugin);
        this.setName("Nearby Portals");
        this.setCommandUsage("/mvp info " + ChatColor.GREEN + "{RADIUS}");
        this.setArgRange(1, 1);
        this.addKey("mvp nearby");
        this.addKey("mvn");
        this.addKey("mvpnearby");
        this.setPermission("multiverse.portal.nearby", "Displays nearby portals", PermissionDefault.OP);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this.");
            return;
        }

        Player player = (Player) sender;
        Location playerLoc = player.getLocation();
        String world = player.getWorld().getName();

        int radius = NumberConversions.toInt(args.get(0));
        if (radius < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid radius: " + args.get(0));
            return;
        }

        // This could probably stand to be optimized
        Map<MVPortal, Double> nearby = new HashMap<MVPortal, Double>();
        List<MVPortal> portals = plugin.getPortalManager().getAllPortals();
        for (MVPortal portal : portals) {
            PortalLocation location = portal.getLocation();
            if (location.getMVWorld().getName().equals(world)) {
                double distance = location.distance(playerLoc);
                if (distance <= radius) {
                    nearby.put(portal, distance);
                }
            }
        }

        if (nearby.size() > 0) {
            for (Entry<MVPortal, Double> entry : nearby.entrySet()) {
                player.sendMessage(ChatColor.AQUA + " - " + ChatColor.WHITE + entry.getKey().getName() +
                        ChatColor.AQUA + "  (" + ChatColor.WHITE + entry.getValue() + ChatColor.AQUA + " blocks)");
            }
        } else {
            player.sendMessage(ChatColor.AQUA + "No portals within " + ChatColor.WHITE + radius + ChatColor.AQUA + " blocks.");
        }
    }
}
