package turbo.bladeball.gameplay.util.command.skill;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.skill.SkillListener;
import turbo.bladeball.gameplay.skill.ability.PullSkill;
import turbo.bladeball.gameplay.util.ballUtil.TargetPlayer;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PullCommand implements CommandExecutor {

    SkillListener skillManager;
    TargetPlayer targetPlayer;
    BallConfig ballConfig;

    @Autowired
    public PullCommand(SkillListener skillListener, TargetPlayer targetPlayer, BallConfig ballConfig) {
        this.skillManager = skillListener;
        this.targetPlayer = targetPlayer;
        this.ballConfig = ballConfig;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("pull")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                Skill skill = new PullSkill(targetPlayer, ballConfig);
                skillManager.unlockSkill(player.getUniqueId(), skill);
                skillManager.equipSkill(player.getUniqueId(), skill);
            }
            return true;
        }
        return false;
    }
}
