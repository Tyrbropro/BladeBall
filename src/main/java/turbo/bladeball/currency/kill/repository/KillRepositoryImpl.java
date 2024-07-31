package turbo.bladeball.currency.kill.repository;

import turbo.bladeball.currency.kill.KillInfo;

public class KillRepositoryImpl implements KillRepository {
    KillInfo killInfo = new KillInfo();

    @Override
    public void setKill(int kill) {
        killInfo.setKill(kill);
    }

    @Override
    public int getKill() {
        return killInfo.getKill();
    }
}
