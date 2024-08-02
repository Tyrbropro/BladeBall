package turbo.bladeball.gameplay.ball;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import turbo.bladeball.BladeBall;
import turbo.bladeball.PlayerData;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.currency.kill.repository.KillRepositoryImpl;
import turbo.bladeball.currency.lose.repository.LoseRepositoryImpl;
import turbo.bladeball.currency.money.repository.MoneyRepositoryImpl;
import turbo.bladeball.currency.win.repository.WinRepositoryImpl;
import turbo.bladeball.gameplay.util.ballUtil.TargetPlayer;

import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MoveBall {
    BallConfig ballConfig;
    TargetPlayer targetPlayer;

    public MoveBall(BallConfig ballConfig , TargetPlayer targetPlayer) {
        this.ballConfig = ballConfig;
        this.targetPlayer = targetPlayer;
    }

    public void move() {
        Slime slime = ballConfig.getSlime();
        ballConfig.setTarget(targetPlayer.randomPlayer());

        if (checkTarget()) return;
        initializeBukkitRunnable(slime);
    }
    private void glowing() {
        ballConfig.getTarget().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1));
        if (ballConfig.getNoTarget() != null && ballConfig.getNoTarget().hasPotionEffect(PotionEffectType.GLOWING)) {
            ballConfig.getNoTarget().removePotionEffect(PotionEffectType.GLOWING);
        }
    }

    private boolean checkTarget() {
        if (ballConfig.getTarget() == null) return true;
        for (Player player : ballConfig.getPlayers()) {
            if (player.equals(ballConfig.getTarget())) {
                return false;
            }
        }
        return true;
    }
    private void initializeBukkitRunnable(Slime slime) {
        ballConfig.setStart(ballConfig.getSlime().getLocation().toVector());
        ballConfig.setPlayerHitDirection(ballConfig.getStart());
        if (ballConfig.getNoTarget() != null)
            ballConfig.setPlayerHitDirection(ballConfig.getNoTarget().getLocation().getDirection().normalize());

        ballConfig.setSpeed(2);
        ballConfig.setTick(0);

        ballConfig.setCurrentPosition(ballConfig.getStart().clone());
        ballConfig.setInitialDirection(ballConfig.getPlayerHitDirection().clone());
        new BukkitRunnable() {
            @Override
            public void run() {
                moveSlimeTowardsTarget(this, slime, ballConfig.getStart(), ballConfig.getCurrentPosition(), ballConfig.getInitialDirection());
            }
        }.runTaskTimer(BladeBall.getPlugin(BladeBall.class), 0L, 1L);
    }

    private void moveSlimeTowardsTarget(BukkitRunnable runnable, Slime slime, Vector start, Vector currentPosition, Vector initialDirection) {
        if (checkTarget()) {
            runnable.cancel();
            endInteraction(slime);
            return;
        }
        glowing();
        Vector end = ballConfig.getTarget().getLocation().toVector();

        ballConfig.setTick(ballConfig.getTick() + 1);
        if (ballConfig.getTick() >= 20) {
            ballConfig.setSpeed(ballConfig.getSpeed() * 1.05);
            ballConfig.setTick(ballConfig.getTick() - 20);
        }

        double t = calculateInterpolationFactor(currentPosition, end, start);

        Vector toEnd = end.clone().subtract(currentPosition).normalize();
        Vector currentDirection = toEnd.multiply(t).add(initialDirection.multiply(1 - t)).normalize();

        currentPosition.add(currentDirection.multiply(0.1 * ballConfig.getSpeed()));
        if (currentPosition.getY() < 87) {
            currentPosition.setY(87);
        }
        slime.teleport(new Location(slime.getWorld(), currentPosition.getX(), currentPosition.getY(), currentPosition.getZ()));

        Location targetLoc = ballConfig.getTarget().getLocation();
        if (targetLoc.distance(slime.getLocation()) <= 1.02) {
            endInteraction(slime);
            runnable.cancel();
        }
    }

    private double calculateInterpolationFactor(Vector currentPosition, Vector end, Vector start) {
        double distanceToEnd = currentPosition.distance(end);
        double totalDistance = start.distance(end);

        double t = 1.2 - (distanceToEnd / totalDistance);
        if (t < 0.2) {
            t = 0.2;
        } else if (t >= 1) {
            t = 0.8;
        }
        return t;
    }

    private void endInteraction(Slime slime) {
        if (ballConfig.getNoTarget() != null) {
            giveKillReward();
        }
        giveLoseReward();
        if (ballConfig.getPlayers().size() == 1) {
            List<Player> playerList = new ArrayList<>(ballConfig.getPlayers());

            playerList.get(0).teleport(new Location(playerList.get(0).getWorld(), -190.5, 86, 272.5));
            giveWinReward(playerList.get(0));
        }
        slime.teleport(new Location(slime.getWorld(), -222, 87, 271));
    }

    private void giveKillReward() {
        PlayerData data = PlayerData.getUsers().get(ballConfig.getNoTarget().getUniqueId());
        MoneyRepositoryImpl moneyRepository = data.getMoneyRepository();
        KillRepositoryImpl killRepository = data.getKillRepository();

        moneyRepository.setMoney(moneyRepository.getMoney() + 10);
        killRepository.setKill(killRepository.getKill() + 1);
        ballConfig.getNoTarget().sendMessage("+10 Monet za kill");
    }

    private void giveWinReward(Player player) {
        PlayerData data = PlayerData.getUsers().get(player.getUniqueId());
        MoneyRepositoryImpl moneyRepository = data.getMoneyRepository();
        WinRepositoryImpl winRepository = data.getWinRepository();

        winRepository.setWin(winRepository.getWin() + 1);
        moneyRepository.setMoney(moneyRepository.getMoney() + 30);
        player.sendMessage("+30 Monet za pobedy");
    }

    private void giveLoseReward() {
        if (ballConfig.getTarget() == null) return;

        PlayerData data = PlayerData.getUsers().get(ballConfig.getTarget().getUniqueId());
        LoseRepositoryImpl loseRepository = data.getLoseRepository();
        loseRepository.setLose(loseRepository.getLose() + 1);

        ballConfig.getPlayers().remove(ballConfig.getTarget());
        ballConfig.getTarget().sendMessage("Вы проиграли");
        ballConfig.getTarget().setHealth(0.0);
        ballConfig.setTarget(null);
    }
}