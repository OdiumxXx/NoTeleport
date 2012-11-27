package me.odium.noteleport.commands;

import java.util.List;
import me.odium.noteleport.NoTeleport;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class tpaccept implements CommandExecutor {   

  public NoTeleport plugin;
  public tpaccept(NoTeleport plugin)  {
    this.plugin = plugin;
  }


  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {    
    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }
    if (player == null) {
      plugin.output(sender, "OnlyByPlayer", "");
      return true;
    }
    if (args.length == 0) {
      if (!plugin.CallMap.containsKey(player)) {
        plugin.output(player, "NoActiveRequests", "");
        return true;
      } else {

        List<Player> Reqs = plugin.CallMap.get(player);

for (Player req : Reqs) {
  req.teleport(player, TeleportCause.COMMAND);
  plugin.output(req, "RequestAccepted", player.getName());
  plugin.output(player, "RequestAccepted", req.getName());
}

//        while (Reqs.iterator().hasNext()) {
//          Player req = Reqs.iterator().next();
//          req.teleport(player, TeleportCause.COMMAND);
//          plugin.output(req, "RequestAccepted", player.getName());
//          plugin.output(player, "RequestAccepted", req.getName());
//        }

        plugin.CallMap.remove(player);
        return true;
      }
      // ACCEPT SINGLE USER
    } else if (args.length == 1) {  

      if (!plugin.CallMap.containsKey(player)) {
        plugin.output(player, "NoActiveRequests", "");
        return true;
      } else {

        String targetName = plugin.myGetPlayerName(args[0]);
        if (Bukkit.getPlayer(targetName) == null) {
          plugin.output(player, "PlayerNotOnline", targetName);
          return true;
        }      
        Player target = Bukkit.getPlayer(targetName);

        List<Player> Reqs = plugin.CallMap.get(player);
        if (Reqs.contains(target)) {
          target.teleport(player, TeleportCause.COMMAND);
          plugin.output(target, "RequestAccepted", player.getName());
          plugin.output(player, "RequestAccepted", target.getName());
          return true;
        } else {
          plugin.output(player, "HasNotSentCall", target.getName());          
          return true;
        }

      }

    }
    return true;
  }
}