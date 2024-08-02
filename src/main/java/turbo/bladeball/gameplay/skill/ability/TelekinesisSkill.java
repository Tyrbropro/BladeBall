package turbo.bladeball.gameplay.skill.ability;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import turbo.bladeball.BladeBall;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.util.ballUtil.TargetPlayer;

public class TelekinesisSkill extends Skill {

    public TelekinesisSkill(TargetPlayer targetPlayer, BallConfig ballConfig) {
        super("Telekinesis", 50 * 20, targetPlayer, ballConfig);
    }

    @Override
    public void activate(Player player) {
        Player target = targetPlayer.randomPlayer();
        while (player.equals(target)) target = targetPlayer.randomPlayer();
        double speed = ballConfig.getSpeed();

        ballConfig.setSpeed(0.0);
        player.sendMessage("Passive active");
        new BukkitRunnable() {
            @Override
            public void run() {
                ballConfig.setSpeed(speed);
                ballConfig.setTarget(targetPlayer.randomPlayer());
                player.sendMessage("Passive end");
            }
        }.runTaskLater(BladeBall.getPlugin(), 60);
    }
}
