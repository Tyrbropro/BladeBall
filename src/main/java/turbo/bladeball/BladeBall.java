package turbo.bladeball;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.config.SkillConfig;
import turbo.bladeball.gameplay.ball.BallListener;
import turbo.bladeball.gameplay.ball.MoveBall;
import turbo.bladeball.gameplay.skill.SkillListener;
import turbo.bladeball.gameplay.util.MapService;
import turbo.bladeball.gameplay.util.ballUtil.TargetPlayer;
import turbo.bladeball.gameplay.util.command.*;
import turbo.bladeball.gameplay.util.command.skill.*;
import turbo.bladeball.gameplay.util.event.Event;

public final class BladeBall extends JavaPlugin {

    SkillConfig skillConfig;
    SkillListener skillListener;
    BallConfig ballConfig;
    TargetPlayer targetPlayer;
    BallListener ballListener;
    MoveBall moveBall;

    @Override
    public void onEnable() {

        World world = MapService.getWorld();

        if (world != null) {
            skillConfig = new SkillConfig();
            skillListener = new SkillListener(skillConfig);
            ballConfig = new BallConfig();
            targetPlayer = new TargetPlayer(ballConfig);
            ballListener = new BallListener(ballConfig, targetPlayer);
            moveBall = new MoveBall(ballConfig, targetPlayer);

            getServer().getPluginManager().registerEvents(new MapService(), this);
            getServer().getPluginManager().registerEvents(new Event(ballListener, ballConfig, skillListener), this);

            getCommand("start").setExecutor(new StartGameCommand(ballConfig, moveBall));
            getCommand("end").setExecutor(new EndGameCommand(ballConfig));
            getCommand("money").setExecutor(new MoneyCommand());
            getCommand("myKill").setExecutor(new KillPlayerCommand());
            getCommand("myWin").setExecutor(new WinCommand());
            getCommand("myLose").setExecutor(new LoseCommand());
            getCommand("pull").setExecutor(new PullCommand(skillListener,targetPlayer));
            getCommand("platform").setExecutor(new PlatformCommand(skillListener));
            getCommand("dash").setExecutor(new DashCommand(skillListener));
            getCommand("superJump").setExecutor(new SuperJumpCommand(skillListener));
            getCommand("swap").setExecutor(new SwapCommand(skillListener,targetPlayer));
            getCommand("windCloak").setExecutor(new WindCloakCommand(skillListener));
            getCommand("wayPoint").setExecutor(new WayPointCommand(skillListener));
            getCommand("titanBlade").setExecutor(new TitanBladeCommand(skillListener,ballConfig));
            getCommand("thunderDash").setExecutor(new ThunderDashCommand(skillListener));

            ballListener.spawnBall();

        } else Bukkit.shutdown();
    }

    @Override
    public void onDisable() {
        ballListener.removeBall();
    }
}
