package turbo.bladeball.currency.win.repository;

import turbo.bladeball.currency.win.WinInfo;

public class WinRepositoryImpl implements WinRepository {
    WinInfo winInfo = new WinInfo();
    @Override
    public void setWin(int win) {
        winInfo.setWin(win);
    }

    @Override
    public int getWin() {
        return winInfo.getWin();
    }
}
