package turbo.bladeball.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import turbo.bladeball.gameplay.ball.BallListener;
import turbo.bladeball.gameplay.ball.MoveBall;
import turbo.bladeball.gameplay.skill.SkillListener;
import turbo.bladeball.gameplay.util.MapService;
import turbo.bladeball.gameplay.util.ball.TargetPlayer;
import turbo.bladeball.gameplay.util.command.*;
import turbo.bladeball.gameplay.util.command.skill.*;
import turbo.bladeball.gameplay.util.event.PlayerDamageEvents;
import turbo.bladeball.gameplay.util.event.PlayerInteractionEvents;
import turbo.bladeball.gameplay.util.event.PlayerJoinQuitEvents;
import turbo.bladeball.register.CommandServiceImpl;
import turbo.bladeball.register.ListenerServiceImpl;

@Configuration
@AllArgsConstructor
public class Config {

    @Bean
    public ListenerServiceImpl listenerService() {
        return new ListenerServiceImpl();
    }

    @Bean
    public MapService mapService() {
        return new MapService();
    }

    @Bean
    public CommandServiceImpl commandService() {
        return new CommandServiceImpl();
    }

    @Bean
    public SkillConfig skillConfig() {
        return new SkillConfig();
    }

    @Bean
    public BallConfig ballConfig() {
        return new BallConfig();
    }

    @Bean
    public TargetPlayer targetPlayer() {
        return new TargetPlayer(ballConfig());
    }

    @Bean
    public SkillListener skillListener() {
        return new SkillListener(skillConfig());
    }

    @Bean
    public MoveBall moveBall() {
        return new MoveBall(ballConfig(), targetPlayer());
    }

    @Bean
    public BallListener ballListener() {
        return new BallListener(ballConfig(), targetPlayer());
    }

    @Bean
    public DashCommand dashCommand() {
        return new DashCommand(skillListener());
    }

    @Bean
    public PlatformCommand platformCommand() {
        return new PlatformCommand(skillListener());
    }

    @Bean
    public PullCommand pullCommand() {
        return new PullCommand(skillListener(), targetPlayer(), ballConfig());
    }

    @Bean
    public SuperJumpCommand superJumpCommand() {
        return new SuperJumpCommand(skillListener());
    }

    @Bean
    public SwapCommand swapCommand() {
        return new SwapCommand(skillListener(), targetPlayer(), ballConfig());
    }

    @Bean
    public TelekinesisCommand telekinesisCommand() {
        return new TelekinesisCommand(skillListener(), targetPlayer(), ballConfig());
    }

    @Bean
    public ThunderDashCommand thunderDashCommand() {
        return new ThunderDashCommand(skillListener());
    }

    @Bean
    public TitanBladeCommand titanBladeCommand() {
        return new TitanBladeCommand(skillListener(), ballConfig());
    }

    @Bean
    public WayPointCommand wayPointCommand() {
        return new WayPointCommand(skillListener());
    }

    @Bean
    public WindCloakCommand windCloakCommand() {
        return new WindCloakCommand(skillListener());
    }

    @Bean
    public EndGameCommand endGameCommand() {
        return new EndGameCommand(ballConfig());
    }

    @Bean
    public StartGameCommand startGameCommand() {
        return new StartGameCommand(ballConfig(), moveBall());
    }

    @Bean
    public MoneyCommand moneyCommand() {
        return new MoneyCommand();
    }

    @Bean
    public KillPlayerCommand killPlayerCommand() {
        return new KillPlayerCommand();
    }

    @Bean
    public LoseCommand loseCommand() {
        return new LoseCommand();
    }

    @Bean
    public WinCommand winCommand() {
        return new WinCommand();
    }

    @Bean
    public PlayerDamageEvents playerDamageEvents() {
        return new PlayerDamageEvents(ballConfig());
    }

    @Bean
    public PlayerInteractionEvents playerInteractionEvents() {
        return new PlayerInteractionEvents(ballListener(), skillListener());
    }

    @Bean
    public PlayerJoinQuitEvents playerJoinQuitEvents() {
        return new PlayerJoinQuitEvents(ballConfig());
    }
}