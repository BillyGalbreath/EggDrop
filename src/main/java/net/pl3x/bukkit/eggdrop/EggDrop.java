package net.pl3x.bukkit.eggdrop;

import net.pl3x.bukkit.eggdrop.command.CmdEggDrop;
import net.pl3x.bukkit.eggdrop.configuration.Config;
import net.pl3x.bukkit.eggdrop.configuration.Lang;
import net.pl3x.bukkit.eggdrop.listener.BukkitListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class EggDrop extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        Config.reload();
        Lang.reload();

        getServer().getPluginManager().registerEvents(new BukkitListener(), this);

        getCommand("eggdrop").setExecutor(new CmdEggDrop(this));

        Logger.info(getName() + " v" + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        Logger.info(getName() + " disabled.");
    }

    public static EggDrop getPlugin() {
        return EggDrop.getPlugin(EggDrop.class);
    }
}
