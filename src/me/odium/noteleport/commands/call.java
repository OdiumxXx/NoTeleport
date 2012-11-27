package me.odium.noteleport.commands;

import java.util.ArrayList;
import java.util.List;

import me.odium.noteleport.NoTeleport;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class call implements CommandExecutor {   

  public NoTeleport plugin;
  public call(NoTeleport plugin)  {
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
      return false;
    } else if (args.length == 1) {
      String targetName = plugin.myGetPlayerName(args[0]);
      List<Player> reqs =  new ArrayList<Player>();

      if (Bukkit.getPlayer(targetName) == null) {
        plugin.output(player, "PlayerNotOnline", targetName);
        return true;
      }      
      Player target = Bukkit.getPlayer(targetName);
      String playerName = player.getName();

      plugin.output(target, "RequestReceived", playerName);
      plugin.output(player, "RequestSent", targetName);

      // set hashmap key playername and value targetname
      if (plugin.CallMap.containsKey(target)) {
        reqs = plugin.CallMap.get(target);
      }
      reqs.add(player);
      plugin.CallMap.put(target, reqs);
      return true;
    }
    return true;
  }

}