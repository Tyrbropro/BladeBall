package turbo.bladeball.gameplay.util.event;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import turbo.bladeball.config.BallConfig;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerDamageEvents implements Listener {
    BallConfig ballConfig;

    @Autowired
    public PlayerDamageEvents(BallConfig ballConfig) {
        this.ballConfig = ballConfig;
    }

    @EventHandler
    public void onPlayerHit(EntityDamageEvent event) {
        if (event.getEntity() == ballConfig.getSlime() || event.getEntity() instanceof Player)
            event.setCancelled(true);
    }
}
