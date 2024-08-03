package turbo.bladeball.gameplay.skill.ability;

import de.slikey.effectlib.effect.LineEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import turbo.bladeball.gameplay.skill.Skill;

public class ThunderDashSkill extends Skill {

    public ThunderDashSkill() {
        super("ThunderDashSkill", 10 * 20, 0);
    }

    @Override
    public void activate(Player player) {
        Location location = player.getLocation();
        Vector direction = location.getDirection();
        Location newLocation = location.add(direction.multiply(6));
        newLocation.setY(86);
        player.teleport(newLocation);
    }

    @Override
    public void activateEffect(Player player) {
        Location location = player.getLocation();
        Vector direction = location.getDirection();
        Location newLocation = location.clone().add(direction.multiply(6));

        LineEffect lineEffect = new LineEffect(em);

        lineEffect.setLocation(location);
        lineEffect.setTarget(newLocation);

        lineEffect.particle = Particle.DRAGON_BREATH;
        lineEffect.particleCount = 50;
        lineEffect.iterations = 1;

        lineEffect.start();
    }
}