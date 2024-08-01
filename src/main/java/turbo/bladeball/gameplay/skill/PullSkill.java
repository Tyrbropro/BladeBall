package turbo.bladeball.gameplay.skill;

import org.bukkit.entity.Player;
import turbo.bladeball.gameplay.ball.MoveBall;

public class PullSkill extends Skill {

    public PullSkill() {
        super("Pull", 30 * 20);
    }

    @Override
    public void activate(Player player) {
        MoveBall moveBall = new MoveBall();
        moveBall.setTarget(player);
    }
}
