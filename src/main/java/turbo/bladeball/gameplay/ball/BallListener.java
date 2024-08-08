package turbo.bladeball.gameplay.ball;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import turbo.bladeball.BladeBall;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.util.ball.TargetPlayer;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE ,makeFinal = true)
public class BallListener {

    BallConfig ballConfig;
    TargetPlayer targetPlayer;

    @Autowired
    public BallListener(BallConfig ballConfig, TargetPlayer targetPlayer) {
        this.ballConfig = ballConfig;
        this.targetPlayer = targetPlayer;
    }

    public void spawnBall() {
        ballConfig.getSlime().setCustomName("Мяч");
        ballConfig.getSlime().setCustomNameVisible(true);
        ballConfig.getSlime().setSize(1);
        ballConfig.getSlime().setGravity(false);
        ballConfig.getSlime().setAI(false);
    }

    public void removeBall() {
        ballConfig.getSlime().remove();
    }

    public void interactBall(Player player) {
        if (hasEffect(player)) return;

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 10) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 0));
                    this.cancel();
                    return;
                }
                if ((player == ballConfig.getTarget() || ballConfig.getTarget() == null) && checkDistance(player)) {
                    targetPlayer.changeTarget(player);
                    this.cancel();
                    return;
                }
                ticks++;
            }
        }.runTaskTimer(BladeBall.getPlugin(), 0L, 1L);
    }

    private boolean checkDistance(Player player) {
        ballConfig.setHitDistant(ballConfig.getTouchDistant().get(player.getUniqueId()));
        Location playerLocation = player.getLocation();
        Location slimeLocation = ballConfig.getSlime().getLocation();

        double distanceBetween = playerLocation.distance(slimeLocation);
        return distanceBetween <= ballConfig.getHitDistant();
    }

    private boolean hasEffect(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.WEAKNESS)) {
                return true;
            }
        }
        return false;
    }
}
