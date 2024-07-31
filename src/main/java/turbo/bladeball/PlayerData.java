package turbo.bladeball;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import turbo.bladeball.currency.kill.repository.KillRepositoryImpl;
import turbo.bladeball.currency.lose.repository.LoseRepositoryImpl;
import turbo.bladeball.currency.money.repository.MoneyRepositoryImpl;
import turbo.bladeball.currency.win.repository.WinRepositoryImpl;

import java.util.HashMap;
import java.util.UUID;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PlayerData {
    @Getter
    static HashMap<UUID, PlayerData> users = new HashMap<>();

    UUID uuid;
    MoneyRepositoryImpl moneyRepository;
    KillRepositoryImpl killRepository;
    WinRepositoryImpl winRepository;
    LoseRepositoryImpl loseRepository;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.moneyRepository = new MoneyRepositoryImpl();
        this.killRepository = new KillRepositoryImpl();
        this.winRepository = new WinRepositoryImpl();
        this.loseRepository = new LoseRepositoryImpl();
    }
}
