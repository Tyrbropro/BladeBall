package turbo.bladeball.gameplay.skill;

import de.slikey.effectlib.EffectManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import turbo.bladeball.BladeBall;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.util.ballUtil.TargetPlayer;

@Getter
public abstract class Skill {
    private final String name;
    private final int cooldown;
    protected BallConfig ballConfig;
    protected TargetPlayer targetPlayer;
    protected EffectManager em;

    public Skill(String name, int cooldown) {
        this.name = name;
        this.cooldown = cooldown;
        this.em = new EffectManager(BladeBall.getPlugin());
    }

    public Skill(String name, int cooldown, BallConfig ballConfig) {
        this.name = name;
        this.cooldown = cooldown;
        this.ballConfig = ballConfig;
        this.em = new EffectManager(BladeBall.getPlugin());
    }

    public Skill(String name, int cooldown, TargetPlayer targetPlayer) {
        this.name = name;
        this.cooldown = cooldown;
        this.targetPlayer = targetPlayer;
        this.em = new EffectManager(BladeBall.getPlugin());
    }

    public Skill(String name, int cooldown, TargetPlayer targetPlayer, BallConfig ballConfig) {
        this.name = name;
        this.cooldown = cooldown;
        this.targetPlayer = targetPlayer;
        this.ballConfig = ballConfig;
        this.em = new EffectManager(BladeBall.getPlugin());
    }


    public abstract void activate(Player player);

   // public abstract void activateEffect(Player player);

    public boolean canUse(Player player) {
        return !player.hasPotionEffect(PotionEffectType.LUCK);
    }

    public void use(Player player) {
        if (canUse(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, cooldown, 1));
            player.sendMessage("active");
            activate(player);
           // activateEffect(player);
        } else player.sendMessage("У вас cooldown");
    }
    public EffectManager getEm(){
        return em;
    }
}
