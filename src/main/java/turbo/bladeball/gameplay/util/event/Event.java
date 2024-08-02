package turbo.bladeball.gameplay.util.event;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffectType;
import turbo.bladeball.PlayerData;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.ball.BallListener;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.skill.SkillListener;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Event implements Listener {
    BallListener managerBall;
    BallConfig ballConfig;
    SkillListener skillListener;
    public Event(BallListener managerBall, BallConfig ballConfig , SkillListener skillListener){
        this.managerBall = managerBall;
        this.ballConfig = ballConfig;
        this.skillListener = skillListener;
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().toString().contains("LEFT_CLICK")) {
            managerBall.interactBall(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerHit(EntityDamageEvent event) {
        if (event.getEntity() == ballConfig.getSlime() || event.getEntity() instanceof Player)
            event.setCancelled(true);
    }

    @EventHandler
    public void joinPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        player.setLevel(0);

        ballConfig.getTouchDistant().putIfAbsent(uuid, 3);
        PlayerData.getUsers().putIfAbsent(uuid,new PlayerData(uuid));

        if (player.hasPotionEffect(PotionEffectType.GLOWING)) {
            player.removePotionEffect(PotionEffectType.GLOWING);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Skill skill = skillListener.getEquippedSkill(uuid);
        if (skill != null) skill.use(player);
        else player.sendMessage("У вас нету скилла");
    }

    @EventHandler
    public void quitPlayer(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (Player players : ballConfig.getPlayers()) {
            if (player.equals(players)) {
                ballConfig.getPlayers().remove(player);
            }
        }
    }
}
