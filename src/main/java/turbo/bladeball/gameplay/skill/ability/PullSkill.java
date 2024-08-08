package turbo.bladeball.gameplay.skill.ability;

import de.slikey.effectlib.effect.LineEffect;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.util.ball.TargetPlayer;

public class PullSkill extends Skill {

    public PullSkill(TargetPlayer targetPlayer, BallConfig ballConfig) {
        super("Pull", 30 * 20, 0.3f, targetPlayer, ballConfig);
    }

    @Override
    public void activate(Player player) {
        targetPlayer.setTarget(player);
    }

    @Override
    public void activateEffect(Player player) {
        LineEffect lineEffect = new LineEffect(em);

        lineEffect.setLocation(ballConfig.getSlime().getLocation());
        lineEffect.setTarget(player.getLocation());

        lineEffect.particle = Particle.CRIT;
        lineEffect.particleCount = 50;
        lineEffect.iterations = 1;

        lineEffect.start();
    }
}
