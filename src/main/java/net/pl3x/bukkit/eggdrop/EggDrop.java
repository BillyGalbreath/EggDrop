package net.pl3x.bukkit.eggdrop;

import net.pl3x.bukkit.eggdrop.command.CmdEggDrop;
import net.pl3x.bukkit.eggdrop.configuration.Config;
import net.pl3x.bukkit.eggdrop.configuration.Lang;
import net.pl3x.bukkit.eggdrop.listener.BukkitListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class EggDrop extends JavaPlugin {
    public static final ItemStack EGG_TOTEM = new ItemStack(Material.TOTEM_OF_UNDYING);

    static {
        ItemMeta meta = EGG_TOTEM.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&oEgg Totem"));
        meta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', "&7&oKill an entity while holding this"),
                ChatColor.translateAlternateColorCodes('&', "&7&oto drop it's spawn egg on death")
        ));
        meta.addEnchant(Enchantment.DURABILITY, 10, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        EGG_TOTEM.setItemMeta(meta);
    }

    @Override
    public void onEnable() {
        Config.reload(this);
        Lang.reload(this);

        getServer().getPluginManager().registerEvents(new BukkitListener(), this);

        getCommand("eggdrop").setExecutor(new CmdEggDrop(this));
    }
}
