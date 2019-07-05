package net.pl3x.bukkit.eggdrop.listener;

import net.pl3x.bukkit.eggdrop.EggDrop;
import net.pl3x.bukkit.eggdrop.configuration.Lang;
import net.pl3x.purpur.event.PlayerSetSpawnerTypeWithEggEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class BukkitListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        switch (event.getEntityType()) {
            case PLAYER:
                return; // we do not care if player dies
            case ENDER_DRAGON:
            case WITHER:
            case GIANT:
            case ILLUSIONER:
                return; // do not give boss eggs
            default:
        }

        LivingEntity entity = event.getEntity();
        if (!entity.hasAI()) {
            return; // entity does not have AI. ignore
        }

        if (entity.fromMobSpawner()) {
            return; // entity was from spawner cage. ignore
        }

        Player player = entity.getKiller();
        if (player == null) {
            return; // not killed by a player
        }

        ItemStack spawnEgg = tradeTotemForEgg(player, event.getEntityType());
        if (spawnEgg == null) {
            return; // unable to get spawn egg
        }

        // clear the drops and exp
        event.getDrops().clear();
        event.setDroppedExp(0);

        // add the egg to drops
        event.getDrops().add(spawnEgg);
    }

    private ItemStack tradeTotemForEgg(Player player, EntityType type) {
        ItemStack spawnEgg = null;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.isSimilar(EggDrop.EGG_TOTEM)) {
            spawnEgg = Bukkit.getItemFactory().getMonsterEgg(type);
        }

        if (spawnEgg == null) {
            item = player.getInventory().getItemInOffHand();
            if (item.isSimilar(EggDrop.EGG_TOTEM)) {
                spawnEgg = Bukkit.getItemFactory().getMonsterEgg(type);
            }
        }

        if (spawnEgg != null) {
            // only take away the totem if we created a spawn egg to give
            item.subtract();
        }

        return spawnEgg;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityResurrect(EntityResurrectEvent event) {
        LivingEntity entity = event.getEntity();
        EntityEquipment equipment = entity.getEquipment();
        if (equipment != null) {
            if (equipment.getItemInMainHand().isSimilar(EggDrop.EGG_TOTEM) ||
                    equipment.getItemInOffHand().isSimilar(EggDrop.EGG_TOTEM)) {
                event.setCancelled(true); // holding an egg totem, cancel resurrection
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerSetSpawnerTypeWithEggEvent event) {
        switch (event.getEntityType()) {
            case ZOMBIE:
            case SKELETON:
            case SPIDER:
            case CAVE_SPIDER:
            case BLAZE:
            case SILVERFISH:
                return; // allow only these entity types
            default:
                Lang.send(event.getPlayer(), Lang.CANNOT_SET_SPAWNER);
                event.setCancelled(true);
        }
    }
}
