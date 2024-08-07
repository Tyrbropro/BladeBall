package turbo.bladeball.gameplay.util.command.skill;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.skill.SkillListener;
import turbo.bladeball.gameplay.skill.ability.WayPointSkill;
import turbo.bladeball.register.SubCommand;

@Component
public class WayPointCommand implements CommandExecutor {

    SkillListener skillManager;

    @Autowired
    public WayPointCommand(SkillListener skillListener){
        this.skillManager = skillListener;
    }


    @Override
    @SubCommand("wayPoint")
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("wayPoint")) {
            if (commandSender instanceof Player player) {
                Skill skill = new WayPointSkill();
                skillManager.unlockSkill(player.getUniqueId(), skill);
                skillManager.equipSkill(player.getUniqueId(), skill);
            }
            return true;
        }
        return false;
    }
}
