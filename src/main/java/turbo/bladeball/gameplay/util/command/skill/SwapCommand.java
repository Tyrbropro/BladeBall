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
import turbo.bladeball.gameplay.skill.ability.SwapSkill;
import turbo.bladeball.gameplay.util.ballUtil.TargetPlayer;
import turbo.bladeball.register.SubCommand;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SwapCommand implements CommandExecutor {

    SkillListener skillManager;
    TargetPlayer targetPlayer;
    BallConfig ballConfig;

    @Autowired
    public SwapCommand(SkillListener skillListener, TargetPlayer targetPlayer, BallConfig ballConfig) {
        this.skillManager = skillListener;
        this.targetPlayer = targetPlayer;
        this.ballConfig = ballConfig;
    }

    @Override
    @SubCommand("swap")
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("swap")) {
            if (commandSender instanceof Player player) {
                Skill skill = new SwapSkill(targetPlayer, ballConfig);
                skillManager.unlockSkill(player.getUniqueId(), skill);
                skillManager.equipSkill(player.getUniqueId(), skill);
            }
            return true;
        }
        return false;
    }
}
