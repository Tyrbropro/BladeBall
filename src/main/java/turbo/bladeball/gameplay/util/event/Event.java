package turbo.bladeball.gameplay.util.event;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.data.DataBase;
import turbo.bladeball.data.PlayerData;
import turbo.bladeball.gameplay.ball.BallListener;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.skill.SkillListener;

import java.util.UUID;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Event implements Listener {

    BallListener ballListener;
    BallConfig ballConfig;
    SkillListener skillListener;

    @Autowired
    public Event(BallListener ballListener, BallConfig ballConfig, SkillListener skillListener) {
        this.ballListener = ballListener;
        this.ballConfig = ballConfig;
        this.skillListener = skillListener;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().toString().contains("LEFT_CLICK")) {
            ballListener.interactBall(event.getPlayer());
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

        if (player.hasPotionEffect(PotionEffectType.GLOWING)) {
            player.removePotionEffect(PotionEffectType.GLOWING);
        }
    }

    @EventHandler
    public void asyncPlayerLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        PlayerData playerData = DataBase.loadFromMongoDB(uuid);
        PlayerData.getUsers().put(uuid, playerData);
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
        UUID uuid = player.getUniqueId();

        PlayerData playerData = PlayerData.getUsers().get(uuid);

        if (playerData != null) {
            playerData.saveToMongoDB();
        }

        for (Player players : ballConfig.getPlayers()) {
            if (player.equals(players)) {
                ballConfig.getPlayers().remove(player);
            }
        }
    }
}
