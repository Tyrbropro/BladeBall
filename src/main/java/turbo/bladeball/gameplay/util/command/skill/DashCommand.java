package turbo.bladeball.gameplay.util.command.skill;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.skill.SkillListener;
import turbo.bladeball.gameplay.skill.ability.DashSkill;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class DashCommand implements CommandExecutor {

    SkillListener skillManager;

    @Autowired
    public DashCommand(SkillListener skillListener){
        this.skillManager = skillListener;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
            if (command.getName().equalsIgnoreCase("dash")) {
                if (commandSender instanceof Player player) {

                    Skill skill = new DashSkill();
                    skillManager.unlockSkill(player.getUniqueId(), skill);
                    skillManager.equipSkill(player.getUniqueId(), skill);
                }
                return true;
            }
         return false;
    }
}
