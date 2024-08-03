package turbo.bladeball.gameplay.skill.ability;

import de.slikey.effectlib.effect.TraceEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import turbo.bladeball.BladeBall;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.util.ballUtil.TargetPlayer;

public class TelekinesisSkill extends Skill {

    public TelekinesisSkill(TargetPlayer targetPlayer, BallConfig ballConfig) {
        super("Telekinesis", 50 * 20, 0, targetPlayer, ballConfig);
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
                ballConfig.setSpeed(speed * 1.2);
                ballConfig.setTarget(targetPlayer.randomPlayer());
                player.sendMessage("Passive end");
            }
        }.runTaskLater(BladeBall.getPlugin(), 60);
    }

    @Override
    public void activateEffect(Player player) {
        TraceEffect traceEffect = new TraceEffect(em);
        Location ballLoc = ballConfig.getSlime().getLocation();
        traceEffect.setLocation(ballLoc);
        traceEffect.setTarget(ballLoc);
        traceEffect.particle = Particle.EXPLOSION_LARGE;
        traceEffect.particleCount = 50;
        traceEffect.iterations = 3 * 20;
        traceEffect.start();
    }
}
