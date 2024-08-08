package turbo.bladeball.gameplay.util.event;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.data.DataBase;
import turbo.bladeball.data.PlayerData;

import java.util.UUID;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerJoinQuitEvents implements Listener {
    BallConfig ballConfig;

    @Autowired
    public PlayerJoinQuitEvents(BallConfig ballConfig) {
        this.ballConfig = ballConfig;
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
