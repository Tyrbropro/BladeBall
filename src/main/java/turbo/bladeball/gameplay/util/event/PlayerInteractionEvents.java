package turbo.bladeball.gameplay.util.event;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import turbo.bladeball.gameplay.ball.BallListener;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.skill.SkillListener;

import java.util.UUID;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerInteractionEvents implements Listener {
    BallListener ballListener;
    SkillListener skillListener;

    @Autowired
    public PlayerInteractionEvents(BallListener ballListener, SkillListener skillListener) {
        this.ballListener = ballListener;
        this.skillListener = skillListener;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().toString().contains("LEFT_CLICK")) {
            ballListener.interactBall(event.getPlayer());
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
}
