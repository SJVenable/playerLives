package me.sammy.playerLives;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class lifeCommand implements CommandExecutor {

    public DataManager data;

    public lifeCommand(DataManager mainData) {
        this.data = mainData;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            if(s.equalsIgnoreCase("addLives")) {
                Player player = Bukkit.getPlayer(strings[0]);
                try{
                    int nowLives = data.getLives(player.getUniqueId());
                        data.setLives(player.getUniqueId(), nowLives + Integer.parseInt(strings[1]));
                }
                catch(NullPointerException n){
                    commandSender.sendMessage("Please enter a player name");
                }
            }
        }
        return false;
    }
}
