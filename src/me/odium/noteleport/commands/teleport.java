package me.odium.noteleport.commands;

import me.odium.noteleport.NoTeleport;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class teleport implements CommandExecutor {   

  public NoTeleport plugin;
  public teleport(NoTeleport plugin)  {
    this.plugin = plugin;
  }


  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {    
    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }

    if (args.length == 0 && player == null) {
      plugin.displayHelp(sender);
      return true;
    } else if (args.length == 1 && player == null) {
      plugin.output(sender, "OnlyByPlayer", "");
      return true;
    }

    if (args.length == 0) {
      plugin.displayHelp(sender);
      return true;
    } else if (args.length == 1) {
      String targetName = plugin.myGetPlayerName(args[0]);
      if (Bukkit.getPlayer(targetName) == null) {        
        plugin.output(player, "PlayerNotOnline", targetName);
        return true;
      }      

      Player target = Bukkit.getPlayer(targetName);

      // set hashmap key playername and value targetname      
      player.teleport(target, TeleportCause.COMMAND);      
      plugin.output(player, "Teleported", targetName);      
      return true;
    } else if (args.length == 2) {

      if (player == null || player.hasPermission("notp.admin")) {
        String targetName1 = plugin.myGetPlayerName(args[0]);


        String playerName;      
        if (player == null) {
          playerName = "Console";
        } else {
          playerName = player.getName();
        }

        if (Bukkit.getPlayer(targetName1) == null) {        
          plugin.output(player, "PlayerNotOnline", targetName1);
          return true;
        }      
        String targetName2 = plugin.myGetPlayerName(args[1]);

        if (Bukkit.getPlayer(targetName2) == null) {        
          plugin.output(player, "PlayerNotOnline", targetName2);
          return true;
        }      
        Player target1 =Bukkit.getPlayer(targetName1);
        Player target2 =Bukkit.getPlayer(targetName2);
        target1.teleport(target2, TeleportCause.COMMAND);
        plugin.output(target1, "TeleportedBy", playerName);

      } else {
        plugin.output(sender, "NoPermission", "");
        return true;
      }
    }
    return true;
  }

}