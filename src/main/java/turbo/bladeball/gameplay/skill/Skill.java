package turbo.bladeball.gameplay.skill;

import de.slikey.effectlib.EffectManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import turbo.bladeball.BladeBall;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.util.ballUtil.TargetPlayer;

@Getter
public abstract class Skill {
    private final String name;
    private final int cooldown;
    private final float animCooldown;
    protected BallConfig ballConfig;
    protected TargetPlayer targetPlayer;
    protected EffectManager em;

    public Skill(String name, int cooldown, float animCooldown) {
        this.name = name;
        this.cooldown = cooldown;
        this.animCooldown = animCooldown;
        this.em = new EffectManager(BladeBall.getPlugin());
    }
    public Skill(String name, int cooldown, float animCooldown, BallConfig ballConfig) {
        this.name = name;
        this.cooldown = cooldown;
        this.animCooldown = animCooldown;
        this.ballConfig = ballConfig;
        this.em = new EffectManager(BladeBall.getPlugin());
    }
    public Skill(String name, int cooldown, float animCooldown, TargetPlayer targetPlayer) {
        this.name = name;
        this.cooldown = cooldown;
        this.animCooldown = animCooldown;
        this.targetPlayer = targetPlayer;
        this.em = new EffectManager(BladeBall.getPlugin());
    }
    public Skill(String name, int cooldown, float animCooldown, TargetPlayer targetPlayer, BallConfig ballConfig) {
        this.name = name;
        this.cooldown = cooldown;
        this.animCooldown = animCooldown;
        this.targetPlayer = targetPlayer;
        this.ballConfig = ballConfig;
        this.em = new EffectManager(BladeBall.getPlugin());
    }


    public abstract void activate(Player player);

    public abstract void activateEffect(Player player);

    public boolean canUse(Player player) {
        return !player.hasPotionEffect(PotionEffectType.LUCK);
    }

    public void use(Player player) {
        if (canUse(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, cooldown, 1));
            player.sendMessage("active");
            activateEffect(player);
            scheduleActivate(player, animCooldown);
        } else player.sendMessage("У вас cooldown");
    }

    private void scheduleActivate(Player player, float animCooldown) {
        new BukkitRunnable() {
            @Override
            public void run() {
                activate(player);
            }
        }.runTaskLater(BladeBall.getPlugin(), (long) (animCooldown * 20));
    }
}
