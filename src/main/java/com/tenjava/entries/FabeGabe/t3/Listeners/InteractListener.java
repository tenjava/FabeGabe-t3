package com.tenjava.entries.FabeGabe.t3.Listeners;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.util.Random;

public class InteractListener implements Listener {

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action a = e.getAction();
        Random r = new Random();
        int s = r.nextInt(20);
        int x = r.nextInt(40);
        ItemStack hand = p.getItemInHand();
        Location shpSp = new Location(p.getWorld(),
                p.getLocation().getBlockX() + s,
                p.getLocation().getBlockY() + 20,
                p.getLocation().getBlockZ() + x);
        Location fwSp = new Location(p.getWorld(),
                p.getLocation().getBlockX() + r.nextInt(22),
                p.getLocation().getBlockY() + r.nextInt(5),
                p.getLocation().getBlockZ() + x);
        switch(a) {
            case PHYSICAL:
                Sheep shp = (Sheep) p.getWorld().spawnEntity(shpSp, EntityType.SHEEP);
                shp.setSheared(true);
                shp.setCustomNameVisible(true);
                shp.setCustomName(ChatColor.LIGHT_PURPLE + "Swag");
                Firework fw = (Firework) p.getWorld().spawnEntity(fwSp, EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();
                fwm.addEffect(FireworkEffect.builder().
                        flicker(r.nextBoolean())
                        .with(FireworkEffect.Type.STAR).
                                withColor(Color.GREEN).build());
                fw.setFireworkMeta(fwm);
                p.getWorld().createExplosion(fwSp, (float) s);
                break;
            case RIGHT_CLICK_BLOCK:
                if(e.getClickedBlock().getType() == Material.TNT){
                    e.getClickedBlock().setType(Material.AIR);
                    p.sendMessage(ChatColor.RED + "Creating swag bomb...");
                    TNTPrimed tnt = (TNTPrimed) e.getClickedBlock().getWorld().spawnEntity(
                            e.getClickedBlock().getLocation(),
                            EntityType.PRIMED_TNT);
                    tnt.setFuseTicks(100);
                    tnt.setVelocity(new Vector(0, r.nextInt(2), 0));
                    tnt.playEffect(EntityEffect.FIREWORK_EXPLODE);
                } else return;
                break;
            case RIGHT_CLICK_AIR:
                if(hand.getType() == Material.BONE) {
                    Wolf wolf = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
                    wolf.setCustomNameVisible(true);
                    wolf.setCustomName("Yo dawg.");
                    wolf.playEffect(EntityEffect.FIREWORK_EXPLODE);
                    p.playEffect(wolf.getLocation(), Effect.EXTINGUISH, 20);
                } else
                    return;
            default:
                break;
        }
    }

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onEntityInteract(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        Entity ent = e.getRightClicked();
        ItemStack hand = p.getItemInHand();
        if(!(ent instanceof IronGolem))
            return;
            IronGolem golem = (IronGolem) ent;
            if(hand.getType() == Material.ARROW) {
                if(golem.getCustomName() != null){
                    p.sendMessage(ChatColor.RED + "That golem belongs to " + golem.getCustomName());
                    return;
                } else {
                golem.setPlayerCreated(true);
                golem.setCustomNameVisible(true);
                golem.setCustomName(ChatColor.RED + p.getName());
                p.playEffect(golem.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
                }
        }
    }
}