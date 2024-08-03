package turbo.bladeball.gameplay.skill.ability;

import de.slikey.effectlib.effect.LineEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.util.ballUtil.TargetPlayer;

public class SwapSkill extends Skill {

    private Player target;
    public SwapSkill(TargetPlayer targetPlayer, BallConfig ballConfig) {
        super("Swap", 50 * 20, 0.2f, targetPlayer, ballConfig);
    }

    @Override
    public void activate(Player player) {
        Location playerLoc = player.getLocation();
        Location targetLoc = target.getLocation();

        player.teleport(targetLoc);
        target.teleport(playerLoc);

    }

    @Override
    public void activateEffect(Player player) {
        target = targetPlayer.randomPlayer();
        while (player.equals(target)) target = targetPlayer.randomPlayer();

        LineEffect lineEffect = new LineEffect(em);

        lineEffect.setLocation(target.getLocation());
        lineEffect.setTarget(player.getLocation());

        lineEffect.particle = Particle.REDSTONE;
        lineEffect.particleCount = 100;
        lineEffect.iterations = 1;

        lineEffect.start();
    }
}
