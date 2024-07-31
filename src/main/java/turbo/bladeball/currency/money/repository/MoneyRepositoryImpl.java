package turbo.bladeball.currency.money.repository;

import turbo.bladeball.currency.money.MoneyInfo;

public class MoneyRepositoryImpl implements MoneyRepository {
    MoneyInfo moneyInfo = new MoneyInfo();

    @Override
    public void setMoney(int money) {
        moneyInfo.setMoney(money);
    }

    @Override
    public int getMoney() {
        return moneyInfo.getMoney();
    }
}
