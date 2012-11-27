package me.odium.noteleport.commands;

import me.odium.noteleport.NoTeleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class put implements CommandExecutor {   

  public NoTeleport plugin;
  public put(NoTeleport plugin)  {
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

      Block target = player.getTargetBlock(null, 200);
      float Yaw = player.getLocation().getYaw();
      float Pitch = player.getLocation().getPitch();
      Location loc = target.getLocation();
      loc.setY(loc.getY()+1);
      loc.setPitch(Pitch);
      loc.setYaw(Yaw);
      player.teleport(loc, TeleportCause.COMMAND);
      plugin.output(player, "Teleported", "");
      return true;
    } else if (args.length == 1) {
      String targetName = plugin.myGetPlayerName(args[0]);

      if (Bukkit.getPlayer(targetName) == null) {        
        plugin.output(player, "PlayerNotOnline", targetName);
        return true;
      }      

      Player target = Bukkit.getPlayer(targetName);
      String playerName = player.getName();
      Block targetBL = player.getTargetBlock(null, 200);
      float Yaw = player.getLocation().getYaw();
      float Pitch = player.getLocation().getPitch();
      Location loc = targetBL.getLocation();
      loc.setY(loc.getY()+1);
      loc.setPitch(Pitch);
      loc.setYaw(Yaw);
      target.teleport(loc, TeleportCause.COMMAND);
      plugin.output(target, "TeleportedBy", playerName);
      plugin.output(player, "YouTeleported", targetName);
      return true;
    }
    return true;
  }

}