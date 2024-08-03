package turbo.bladeball.gameplay.skill.ability;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import turbo.bladeball.gameplay.skill.Skill;

public class SuperJumpSkill extends Skill {

    public SuperJumpSkill() {
        super("SuperJump", 10 * 20, 0);
    }

    @Override
    public void activate(Player player) {
        Vector velocity = player.getVelocity().setY(4);
        player.setVelocity(velocity);
    }

    @Override
    public void activateEffect(Player player) {
        Location loc = player.getLocation();

        for (int i = 0; i < 5; i++) {
            createCircleEffect(loc.clone().add(0, i * 2, 0));
        }
    }

    private void createCircleEffect(Location location) {
        for (int i = 0; i < 150; i++) {
            double angle = 2 * Math.PI * i / 150;
            double x = location.getX() + 4 * Math.cos(angle);
            double z = location.getZ() + 4 * Math.sin(angle);
            Location particleLocation = new Location(location.getWorld(), x, location.getY(), z);
            location.getWorld().spawnParticle(Particle.FLAME, particleLocation, 0, 0, 0, 0);
        }
    }
}