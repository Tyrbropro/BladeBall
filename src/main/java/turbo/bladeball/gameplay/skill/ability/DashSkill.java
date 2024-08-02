package turbo.bladeball.gameplay.skill.ability;

import org.bukkit.Location;
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
    }
}