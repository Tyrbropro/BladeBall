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
import turbo.bladeball.gameplay.skill.ability.TelekinesisSkill;
import turbo.bladeball.gameplay.util.ball.TargetPlayer;
import turbo.bladeball.register.SubCommand;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TelekinesisCommand implements CommandExecutor {

    SkillListener skillManager;
    TargetPlayer targetPlayer;
    BallConfig ballConfig;

    @Autowired
    public TelekinesisCommand(SkillListener skillListener,TargetPlayer targetPlayer,BallConfig ballConfig){
        this.skillManager = skillListener;
        this.targetPlayer = targetPlayer;
        this.ballConfig = ballConfig;
    }

    @Override
    @SubCommand("telekinesis")
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("telekinesis")) {
            if (commandSender instanceof Player player) {
                Skill skill = new TelekinesisSkill(targetPlayer, ballConfig);
                skillManager.unlockSkill(player.getUniqueId(), skill);
                skillManager.equipSkill(player.getUniqueId(), skill);
            }
            return true;
        }
        return false;
    }
}
