package turbo.bladeball.gameplay.skill.ability;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import turbo.bladeball.BladeBall;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.skill.Skill;

import java.util.UUID;

public class TitanBladeSkill extends Skill {

    public TitanBladeSkill(BallConfig ballConfig) {
        super("TitanBlade", 50 * 20 , ballConfig);
    }

    @Override
    public void activate(Player player) {
        UUID uuid = player.getUniqueId();
        ballConfig.getTouchDistant().put(uuid, 6);
        player.sendMessage("Passive activate");

        new BukkitRunnable() {
            @Override
            public void run() {
                ballConfig.getTouchDistant().put(uuid, 3);
                player.sendMessage("Passive end");
            }
        }.runTaskLater(BladeBall.getPlugin(), 200);
    }
}
