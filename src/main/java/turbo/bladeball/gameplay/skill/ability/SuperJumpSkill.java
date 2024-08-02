package turbo.bladeball.gameplay.skill.ability;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import turbo.bladeball.gameplay.skill.Skill;

public class SuperJumpSkill extends Skill {

    public SuperJumpSkill() {
        super("SuperJump", 10 * 20);
    }

    @Override
    public void activate(Player player) {
        Vector velocity = player.getVelocity().setY(4);
        player.setVelocity(velocity);
    }
}