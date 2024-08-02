package turbo.bladeball.gameplay.skill.ability;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import turbo.bladeball.gameplay.skill.Skill;

public class ThunderDashSkill extends Skill {

    public ThunderDashSkill() {
        super("ThunderDashSkill", 10 * 20);
    }

    @Override
    public void activate(Player player) {
        Location location = player.getLocation();
        Vector direction = location.getDirection();
        Location newLocation = location.add(direction.multiply(6));
        newLocation.setY(86);
        player.teleport(newLocation);
    }
}