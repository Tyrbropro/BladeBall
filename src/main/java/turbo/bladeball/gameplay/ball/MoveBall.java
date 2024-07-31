package turbo.bladeball.gameplay.ball;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import turbo.bladeball.BladeBall;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class MoveBall {

    @Getter
    static final List<Player> players = new ArrayList<>();

    public static Player target;
    public static Player noTarget;

    double speed;
    double tick;

    public void move(Player player) {
        Slime slime = ManagerBall.getSlime();
        noTarget = player;
        target = getNearestPlayerLookingAt(player);
        if (target == null) {
            player.sendMessage("И нахуя ты меня бьешь?");
            player.sendMessage("Посмотри вокруг , никого нету");
            player.sendMessage("С кем ты собрался махаться?");
            return;
        }
        if (!checkTarget()) return;
        initializeBukkitRunnable(slime, player);
    }

    private boolean checkTarget() {
        for (Player player : getPlayers()) {
            if (player.equals(target)) {
                return true;
            }
        }
        return false;
    }
    private Player getNearestPlayerLookingAt(Player player) {
        TargetPlayer targetPlayer = new TargetPlayer();
        return targetPlayer.getNearestPlayerLookingAt(player, 100);
    }

    private void initializeBukkitRunnable(Slime slime, Player player) {
        Vector start = slime.getLocation().toVector();
        Vector playerHitDirection = player.getLocation().getDirection().normalize();

        speed = 6.0;
        tick = 0;
        new BukkitRunnable() {
            final Vector currentPosition = start.clone();
            final Vector initialDirection = playerHitDirection.clone();

            @Override
            public void run() {
                moveSlimeTowardsTarget(this, slime, start, currentPosition, initialDirection);
            }
        }.runTaskTimer(BladeBall.getPlugin(BladeBall.class), 0L, 1L);
    }

    private void moveSlimeTowardsTarget(BukkitRunnable runnable, Slime slime, Vector start, Vector currentPosition, Vector initialDirection) {
        if (target == null) return;
        Vector end = target.getLocation().toVector();

        if (++tick >= 20) {
            speed *= 1.05;
            tick -= 20;
        }

        double t = calculateInterpolationFactor(currentPosition, end, start);

        Vector toEnd = end.clone().subtract(currentPosition).normalize();
        Vector currentDirection = toEnd.multiply(t).add(initialDirection.multiply(1 - t)).normalize();

        currentPosition.add(currentDirection.multiply(0.1 * speed));
        if (currentPosition.getY() < 87) {
            currentPosition.setY(87);
        }
        slime.teleport(new Location(slime.getWorld(), currentPosition.getX(), currentPosition.getY(), currentPosition.getZ()));

        Location targetLoc = target.getLocation();
        if (targetLoc.distance(slime.getLocation()) <= 1.01) {
            endInteraction(slime);
            runnable.cancel();
        }
    }

    private double calculateInterpolationFactor(Vector currentPosition, Vector end, Vector start) {
        double distanceToEnd = currentPosition.distance(end);
        double totalDistance = start.distance(end);

        double t = 1.3 - (distanceToEnd / totalDistance);
        if (t < 0.3) {
            t = 0.3;
        } else if (t >= 1) {
            t = 0.8;
        }
        return t;
    }

    private void endInteraction(Slime slime) {
        getPlayers().remove(target);
        target.sendMessage("ЛОХ ПРОЕБАЛ");
        target.setHealth(0.0);
        target = null;

        if(getPlayers().size() == 1){
            getPlayers().get(0).sendMessage("Вы победили!");
        }

        slime.teleport(new Location(slime.getWorld(), -222, 87, 271));
    }
}