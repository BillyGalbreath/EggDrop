package net.pl3x.bukkit.eggdrop;

import net.pl3x.bukkit.eggdrop.nms.NBTHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtil {
    public static boolean takeItem(Player player, ItemStack itemStack) {
        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (!ItemUtil.equals(itemStack, offHand)) {
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            if (!ItemUtil.equals(itemStack, mainHand)) {
                return false;
            }
            if (mainHand.getAmount() > 1) {
                mainHand.setAmount(mainHand.getAmount() - 1);
            } else {
                mainHand = new ItemStack(Material.AIR);
            }
            player.getInventory().setItemInMainHand(mainHand);
            return true;
        }
        if (offHand.getAmount() > 1) {
            offHand.setAmount(offHand.getAmount() - 1);
        } else {
            offHand = new ItemStack(Material.AIR);
        }
        player.getInventory().setItemInOffHand(offHand);
        return true;
    }

    public static ItemStack getItemStack(ConfigurationSection section) {
        if (section == null) {
            return null;
        }

        Material material = getMaterial(section.getString("material"));
        if (material == null) {
            return null;
        }

        ItemStack itemStack = new ItemStack(material, 1, (short) section.getInt("data", 0));

        if (itemStack.getAmount() == 1) {
            int amount = section.getInt("amount", 1);
            itemStack.setAmount(amount);
        }

        itemStack = NBTHandler.setItemNBT(itemStack, section.getString("nbt"), section.getCurrentPath());

        ItemMeta itemMeta = itemStack.getItemMeta();

        String name = section.getString("name");
        if (name != null && !name.isEmpty()) {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }

        List<String> lore = section.getStringList("lore");
        if (lore != null && !lore.isEmpty()) {
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
            }
            itemMeta.setLore(lore);
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static Material getMaterial(String materialName) {
        if (materialName == null) {
            return null;
        }

        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            return null;
        }

        return material;
    }

    public static boolean equals(ItemStack item1, ItemStack item2) {
        item1 = item1.clone();
        item2 = item2.clone();
        item1.setAmount(1);
        item2.setAmount(1);
        Logger.debug("Item1: " + item1.toString());
        Logger.debug("Item2: " + item2.toString());
        return item1.equals(item2);
    }
}
