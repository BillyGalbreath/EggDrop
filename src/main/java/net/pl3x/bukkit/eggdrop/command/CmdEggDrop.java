package net.pl3x.bukkit.eggdrop.command;

import net.pl3x.bukkit.eggdrop.EggDrop;
import net.pl3x.bukkit.eggdrop.configuration.Config;
import net.pl3x.bukkit.eggdrop.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CmdEggDrop implements TabExecutor {
    private final EggDrop plugin;

    public CmdEggDrop(EggDrop plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("command.eggdrop")) {
            if (args.length == 1) {
                return Stream.of("reload", "givetotem")
                        .filter(arg -> arg.toLowerCase().startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            }
            if (args.length == 2) {
                return Bukkit.getOnlinePlayers().stream()
                        .filter(player -> player.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                        .map(HumanEntity::getName)
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("command.eggdrop")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            return false; // show usage
        }

        if (args[0].equalsIgnoreCase("reload")) {
            Config.reload(plugin);
            Lang.reload(plugin);

            Lang.send(sender, "&d" + plugin.getName() + " v" + plugin.getDescription().getVersion() + " reloaded");
            return true;
        }

        if (args[0].equalsIgnoreCase("givetotem") && args.length > 1) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                Lang.send(sender, Lang.PLAYER_NOT_ONLINE);
                return true;
            }

            target.getInventory().addItem(EggDrop.EGG_TOTEM).forEach((index, stack) -> {
                Item drop = target.getWorld().dropItem(target.getLocation(), stack);
                drop.setPickupDelay(0);
                drop.setOwner(target.getUniqueId());
            });

            Lang.send(sender, Lang.GAVE_TOTEM
                    .replace("{target}", target.getName()));
            return true;
        }

        return false;
    }
}
