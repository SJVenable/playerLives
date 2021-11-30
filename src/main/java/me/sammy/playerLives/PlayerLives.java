package me.sammy.playerLives;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.Objects;

public class PlayerLives extends JavaPlugin implements Listener {

    public DataManager data;
    public static PlayerLives instance;

    @Override
    public void onEnable() {
        this.data = new DataManager(this);
        data.saveDefaultConfig();

        data.getConfig().createSection("PlayerLives");
        data.saveConfig();

        Objects.requireNonNull(this.getCommand("addLives")).setExecutor(new LifeCommand(this.data));
        Objects.requireNonNull(this.getCommand("Revive")).setExecutor(new ReviveCommand(this.data));

        getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(this, this);

        Bukkit.addRecipe(makeLifeItem());
        Bukkit.addRecipe(makeReviveItem());


    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.getPlayer().setPlayerListName(ChatColor.YELLOW + e.getPlayer().getName() + " ( " + data.getLives(player.getUniqueId()) + " )");
        if(data.getConfig().getConfigurationSection("PlayerLives") != null) {
            if (this.data.getConfig().getConfigurationSection("PlayerLives").contains(player.getUniqueId().toString())) data.getConfig().getConfigurationSection("PlayerLives").set(player.getUniqueId().toString(), 5);
        }

        int lives = data.getLives(e.getPlayer().getUniqueId());
        player.sendMessage("Welcome! You have " +  lives + " lives, use them wisely");
        data.saveConfig();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        data.setLives(e.getEntity().getUniqueId(), this.data.getLives(e.getEntity().getUniqueId()) - 1);
        data.saveConfig();
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e, Player p) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            System.out.println("right click registered");
            if(e.getItem().getType() == Material.ELYTRA && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Life Item")) {
                System.out.println("life item found");
                data.setLives(p.getUniqueId(), data.getLives(p.getUniqueId()) + 1);
                p.sendMessage(ChatColor.GREEN + "Life added!");
                //remove life item here

            }
        }
    }

    //make onRightClick for beacon/revive item with chat popup

    //also need tab to work + scoreboard with lives displayed

    public ShapedRecipe makeLifeItem() {
        ItemStack lifeItem = new ItemStack(Material.ELYTRA, 1);
        ItemMeta m = lifeItem.getItemMeta();
        assert m != null;
        m.setDisplayName(ChatColor.AQUA + "Life Item");
        m.setLore(Collections.singletonList(ChatColor.GRAY + "Right-click to gain a life!"));
        lifeItem.setItemMeta(m);

        NamespacedKey key = new NamespacedKey(this, "life_item");
        ShapedRecipe recipe = new ShapedRecipe(key, lifeItem);
        recipe.shape(" A ", " A ", " A ");
        recipe.setIngredient('A', Material.DIAMOND_BLOCK);
        return recipe;

    }

    public ShapedRecipe makeReviveItem() {
        ItemStack reviveItem = new ItemStack(Material.BEACON, 1);
        ItemMeta m = reviveItem.getItemMeta();
        assert m != null;
        m.setDisplayName(ChatColor.AQUA + "Revive Item");
        m.setLore(Collections.singletonList(ChatColor.GRAY + "Place and right-click to use"));
        reviveItem.setItemMeta(m);

        NamespacedKey key = new NamespacedKey(this, "revive_item");
        ShapedRecipe recipe = new ShapedRecipe(key, reviveItem);
        recipe.shape(" A ", " A ", " A ");
        recipe.setIngredient('A', Material.GOLD_BLOCK);
        return recipe;

    }

}
