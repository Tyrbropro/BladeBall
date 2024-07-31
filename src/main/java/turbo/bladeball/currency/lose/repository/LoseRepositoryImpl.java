package turbo.bladeball.currency.lose.repository;

import turbo.bladeball.currency.lose.LoseInfo;

public class LoseRepositoryImpl implements LoseRepository {
    LoseInfo loseInfo = new LoseInfo();

    @Override
    public void setLose(int lose) {
        loseInfo.setLose(lose);
    }

    @Override
    public int getLose() {
        return loseInfo.getLose();
    }
}
