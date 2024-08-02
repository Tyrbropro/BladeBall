package turbo.bladeball.gameplay.skill.ability;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import turbo.bladeball.gameplay.skill.Skill;

public class DashSkill extends Skill {


    public DashSkill() {
        super("Dash", 10 * 20);
    }

    @Override
    public void activate(Player player) {
        Location loc = player.getLocation();
        Vector direction = loc.getDirection();

        player.setVelocity(direction.multiply(3));

        createCircleEffect(loc.clone().add(direction.multiply(-1)));
        createCircleEffect(loc.clone().add(direction.multiply(-2)));
        createCircleEffect(loc.clone().add(direction.multiply(-3)));
    }

    private void createCircleEffect(Location location) {
        int particles = 100;
        double radius = 4.0;

        for (int i = 0; i < particles; i++) {
            double angle = 2 * Math.PI * i / particles;
            double y = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);

            Vector playerDirection = location.getDirection().normalize();
            Vector xAxis = playerDirection.getCrossProduct(new Vector(0, 1, 0)).normalize();
            Vector zAxis = playerDirection.getCrossProduct(xAxis).normalize();

            Location particleLocation = location.clone()
                    .add(xAxis.clone().multiply(y))
                    .add(zAxis.clone().multiply(z));

            for (int j = 0; j < 2; j++) {
                location.getWorld().spawnParticle(Particle.FLAME, particleLocation, 0, 0, 0, 0);
            }
        }
    }
}