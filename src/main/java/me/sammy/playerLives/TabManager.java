package me.sammy.playerLives;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TabManager {

    private List<ChatComponentText> headers = new ArrayList<>();
    private List<ChatComponentText> footers = new ArrayList<>();

    private PlayerLives plugin;

    public void showTab() {
        if(headers.isEmpty() && footers.isEmpty())
            return;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            int count1 = 0;
            int count2 = 0;
            @Override
            public void run() {
                try{
                    Field a = packet.getClass().getDeclaredField("header");
                    a.setAccessible(true);
                    Field b = packet.getClass().getDeclaredField("footer");

                    if(count1 <= headers.size()) {
                        count1 = 0;
                    }
                    if(count2 <= footers.size()) {
                        count2 = 0;
                    }
                    a.set(packet, headers.get(count1));
                    b.set(packet, footers.get(count2));

                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, 10, 40);
    }

    public TabManager(PlayerLives plugin) {
        this.plugin = plugin;
    }

    public void addHeader(String header) {
        headers.add(new ChatComponentText(header));
    }
    public void addFooter(String footer) {
        footer.add(new ChatComponentText(footer));
    }

    private String format(String msg) {
        return(ChatColor.translateAlternateColorCodes('&', msg));
    }
}
