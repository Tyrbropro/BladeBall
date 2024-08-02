package turbo.bladeball;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
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

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class BladeBall extends JavaPlugin {

    private static BladeBall plugin;

    BallListener ballListener;
    SkillConfig skillConfig;
    SkillListener skillListener;
    BallConfig ballConfig;
    TargetPlayer targetPlayer;
    MoveBall moveBall;

    @Override
    public void onEnable() {
        plugin = this;

        World world = MapService.getWorld();

        if (world != null) {

            setupConfigs();
            registerEvents();
            registerCommands();

            ballListener.spawnBall();

        } else Bukkit.shutdown();
    }

    @Override
    public void onDisable() {
        ballListener.removeBall();
    }

    public static BladeBall getPlugin() {
        return plugin;
    }

    private void setupConfigs() {
        skillConfig = new SkillConfig();
        skillListener = new SkillListener(skillConfig);
        ballConfig = new BallConfig();
        targetPlayer = new TargetPlayer(ballConfig);
        ballListener = new BallListener(ballConfig, targetPlayer);
        moveBall = new MoveBall(ballConfig, targetPlayer);
    }

    private void registerCommands() {
        Map<String, CommandExecutor> commands = new HashMap<>();

        commands.put("start", new StartGameCommand(ballConfig, moveBall));
        commands.put("end", new EndGameCommand(ballConfig));
        commands.put("money", new MoneyCommand());
        commands.put("myKill", new KillPlayerCommand());
        commands.put("myWin", new WinCommand());
        commands.put("myLose", new LoseCommand());
        commands.put("pull", new PullCommand(skillListener, targetPlayer));
        commands.put("platform", new PlatformCommand(skillListener));
        commands.put("dash", new DashCommand(skillListener));
        commands.put("superJump", new SuperJumpCommand(skillListener));
        commands.put("swap", new SwapCommand(skillListener, targetPlayer));
        commands.put("windCloak", new WindCloakCommand(skillListener));
        commands.put("wayPoint", new WayPointCommand(skillListener));
        commands.put("titanBlade", new TitanBladeCommand(skillListener, ballConfig));
        commands.put("thunderDash", new ThunderDashCommand(skillListener));
        commands.put("telekinesis", new TelekinesisCommand(skillListener, targetPlayer, ballConfig));

        for (Map.Entry<String, CommandExecutor> entry : commands.entrySet()) {
            getCommand(entry.getKey()).setExecutor(entry.getValue());
        }
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new MapService(), this);
        getServer().getPluginManager().registerEvents(new Event(ballListener, ballConfig, skillListener), this);
    }
}
