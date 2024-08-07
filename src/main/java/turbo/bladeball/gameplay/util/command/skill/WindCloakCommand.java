package turbo.bladeball.gameplay.util.command.skill;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.skill.SkillListener;
import turbo.bladeball.gameplay.skill.ability.WindCloakSkill;
import turbo.bladeball.register.SubCommand;

@Component
public class WindCloakCommand implements CommandExecutor {

    SkillListener skillManager;

    @Autowired
    public WindCloakCommand(SkillListener skillListener){
        this.skillManager = skillListener;
    }

    @Override
    @SubCommand("windCloak")
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("windCloak")) {
            if (commandSender instanceof Player player) {
                Skill skill = new WindCloakSkill();
                skillManager.unlockSkill(player.getUniqueId(), skill);
                skillManager.equipSkill(player.getUniqueId(), skill);
            }
            return true;
        }
        return false;
    }
}
