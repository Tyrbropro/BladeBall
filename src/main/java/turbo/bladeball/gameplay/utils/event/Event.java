package turbo.bladeball.gameplay.utils.event;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import turbo.bladeball.BladeBall;
import turbo.bladeball.PlayerData;
import turbo.bladeball.gameplay.ball.ManagerBall;
import turbo.bladeball.gameplay.ball.MoveBall;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Event implements Listener {
    MoveBall moveBall = new MoveBall();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();

        if (damager instanceof Player && entity == ManagerBall.getSlime()) {
            Player player = (Player) damager;
            if (player == MoveBall.target || MoveBall.target == null) {
                moveBall.move(player);
            }
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().toString().contains("LEFT_CLICK")) {

            if (hasEffect(player)) return;

            new BukkitRunnable() {
                int ticks = 0;

                @Override
                public void run() {
                    if (ticks >= 10) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 0));
                        this.cancel();
                        return;
                    }
                    if ((player == MoveBall.target || MoveBall.target == null) && checkDistance(player)) {
                        moveBall.move(player);
                        this.cancel();
                        return;
                    }
                    ticks++;
                }
            }.runTaskTimer(BladeBall.getPlugin(BladeBall.class), 0L, 1L);
        }
    }

    private boolean hasEffect(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.WEAKNESS)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDistance(Player player) {
        Location playerLocation = player.getLocation();
        Location slimeLocation = ManagerBall.getSlime().getLocation();

        double distanceBetween = playerLocation.distance(slimeLocation);
        return distanceBetween <= 3;
    }

    @EventHandler
    public void onPlayerHit(EntityDamageEvent event) {
        if (event.getEntity() == ManagerBall.getSlime() || event.getEntity() instanceof Player)
            event.setCancelled(true);
    }

    @EventHandler
    public void joinPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        PlayerData data = PlayerData.getUsers().get(uuid);
        if (data == null) PlayerData.getUsers().put(uuid, new PlayerData(uuid));
    }

    @EventHandler
    public void quitPlayer(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (Player players : MoveBall.getPlayers()) {
            if (player.equals(players)) {
                MoveBall.getPlayers().remove(player);
            }
        }
    }
}
