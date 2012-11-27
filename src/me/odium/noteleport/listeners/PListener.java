package me.odium.noteleport.listeners;

import java.util.List;

import me.odium.noteleport.NoTeleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class PListener implements Listener {

  public NoTeleport plugin;
  public PListener(NoTeleport plugin) {    
    this.plugin = plugin;    
    plugin.getServer().getPluginManager().registerEvents(this, plugin);  
  }


  @EventHandler
  public boolean onPlayerTeleport(PlayerTeleportEvent event) {

    Player player = event.getPlayer();
    Location To = event.getTo();
    TeleportCause cause = event.getCause();
    
    if (cause.equals(TeleportCause.COMMAND)) {

      List<Player> players = To.getWorld().getPlayers();

      for (Player pl : players) {
        Location TargetLocation = pl.getLocation();
        if (TargetLocation.equals(To)) {

          Player Target = pl;
          String TargetName = pl.getName().toLowerCase();

          if (plugin.getStorageConfig().getString(TargetName) != null) {

            if (plugin.getStorageConfig().getBoolean(TargetName+".enabled") && !player.hasPermission("notp.exempt")) {


              // CHECK CALLMAP FOR THE TARGET - IF TARGET IS IN CALLMAP
              if (plugin.CallMap.containsKey(Target)) {

                List<Player> reqs = plugin.CallMap.get(Target);
                if (reqs.contains(player)) {

                  // DO NOTHING - GO AHEAD WITH TP
                  reqs.remove(player);                  
                  plugin.CallMap.put(pl, reqs);

                  return true;                  
                }
              }

              // CHECK ACCESS LIST
              List<String> access = plugin.getStorageConfig().getStringList(TargetName+".access");
              for (String acc : access) {
                // IF PLAYER IS IN ACCESS LIST FOR TARGET
                if (acc.equalsIgnoreCase(player.getName().toLowerCase())) {
                  // DO NOTHING - GO AHEAD WITH TP
                  return true;
                }              
              }            
              // CANCEL TELEPORT - TELL PLAYER WHY
              event.setCancelled(true);
              plugin.output(player, "TeleportDenied", Target.getDisplayName());
              return true;
            }

            return true;
          }       
        }
      }
    }

     return true;    

  }
}