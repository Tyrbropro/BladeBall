package turbo.bladeball.gameplay.utils.event;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import turbo.bladeball.BladeBall;
import turbo.bladeball.PlayerData;
import turbo.bladeball.gameplay.ball.ManagerBall;
import turbo.bladeball.gameplay.ball.MoveBall;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.skill.SkillManager;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Event implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        MoveBall moveBall = new MoveBall();
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
                    if ((player == MoveBall.getTarget() || MoveBall.getTarget() == null) && checkDistance(player)) {
                        moveBall.changeTarget(player);
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

        if (player.hasPotionEffect(PotionEffectType.GLOWING)) {
            player.removePotionEffect(PotionEffectType.GLOWING);
        }
    }
    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        SkillManager skillManager = new SkillManager();
        Skill skill = skillManager.getEquippedSkill(uuid);
        if(skill != null) skill.use(player);
        else player.sendMessage("У вас нету скилла");
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
