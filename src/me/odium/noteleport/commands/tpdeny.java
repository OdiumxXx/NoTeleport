package me.odium.noteleport.commands;

import java.util.List;

import me.odium.noteleport.NoTeleport;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class tpdeny implements CommandExecutor {   

  public NoTeleport plugin;
  public tpdeny(NoTeleport plugin)  {
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

      if (plugin.CallMap.get(player) == null) {
        plugin.output(player, "NoActiveRequests", "");
      } else {
        List<Player> Reqs = plugin.CallMap.get(player);
        for (Player req : Reqs) {
          Player requester = req;

          plugin.output(requester, "RequestDenied", player.getName());
          plugin.output(player, "RequestDenied", requester.getName());

        }
        plugin.CallMap.remove(player);
        return true;
      }
      // ACCEPT SINGLE USER
    } else if (args.length == 1) {  

      if (!plugin.CallMap.containsKey(player)) {
        plugin.output(player, "NoActiveRequests", "");
      } else {

        String targetName = plugin.myGetPlayerName(args[0]);
        if (Bukkit.getPlayer(targetName) == null) {
          plugin.output(player, "PlayerNotOnline", targetName);
          return true;
        }      
        Player target = Bukkit.getPlayer(targetName);

        List<Player> Reqs = plugin.CallMap.get(player);
        if (Reqs.contains(target)) {

          plugin.output(target, "RequestDenied", player.getName());
          plugin.output(player, "RequestDenied", target.getName());

          Reqs.remove(target);
          plugin.CallMap.put(player, Reqs);
          return true;
        } else {
          sender.sendMessage("has not sent a call");
          return true;
        }

      }

    }

    return true;
  }
}