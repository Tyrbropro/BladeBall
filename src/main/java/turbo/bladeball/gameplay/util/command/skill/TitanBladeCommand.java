package turbo.bladeball.gameplay.util.command.skill;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.skill.SkillListener;
import turbo.bladeball.gameplay.skill.ability.TitanBladeSkill;

public class TitanBladeCommand implements CommandExecutor {

    SkillListener skillManager;
    BallConfig ballConfig;

    public TitanBladeCommand(SkillListener skillListener,BallConfig ballConfig) {
        this.skillManager = skillListener;
        this.ballConfig = ballConfig;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("titanBlade")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                Skill skill = new TitanBladeSkill(ballConfig);
                skillManager.unlockSkill(player.getUniqueId(),skill);
                skillManager.equipSkill(player.getUniqueId(),skill);
            }
            return true;
        }
        return false;
    }
}
