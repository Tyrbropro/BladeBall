package turbo.bladeball.gameplay.skill;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.util.ballUtil.TargetPlayer;

@Getter
public abstract class Skill {
    private final String name;
    private final int cooldown;
    protected BallConfig ballConfig;
    protected TargetPlayer targetPlayer;

    public Skill(String name, int cooldown) {
        this.name = name;
        this.cooldown = cooldown;
    }

    public Skill(String name, int cooldown, BallConfig ballConfig) {
        this.name = name;
        this.cooldown = cooldown;
        this.ballConfig = ballConfig;
    }

    public Skill(String name, int cooldown, TargetPlayer targetPlayer) {
        this.name = name;
        this.cooldown = cooldown;
        this.targetPlayer = targetPlayer;
    }


    public abstract void activate(Player player);

    public boolean canUse(Player player) {
        return !player.hasPotionEffect(PotionEffectType.LUCK);
    }

    public void use(Player player) {
        if (canUse(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, cooldown, 1));
            player.sendMessage("active");
            activate(player);
        } else player.sendMessage("У вас cooldown");
    }
}
