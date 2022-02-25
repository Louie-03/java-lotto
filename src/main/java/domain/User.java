package domain;

import java.util.ArrayList;
import java.util.List;

public class User {

    private static final int LOTTO_PRICE = 1000;
    private static final String EXCESS_MONEY_ERROR = "금액을 초과하였습니다.";

    private final List<Lotto> lottos;
    private final List<Rank> ranks = new ArrayList<>();
    private final int money;
    private int profit;

    public User(int money, int countOfCustom) {
        this.money = money;
        lottos = null;
    }

    public User(int money, List<Lotto> lottos) {
        this.lottos = lottos;
        if (money / LOTTO_PRICE < lottos.size()) {
            throw new IllegalArgumentException(EXCESS_MONEY_ERROR);
        }
        this.money = money;
    }

    public void buyLotto(Lotto lotto) {
        lottos.add(lotto);
    }

    public void matchWinningLotto(WinningLotto winningLotto) {
        for (Lotto lotto : lottos) {
            int count = lotto.getMatchBallCount(winningLotto);
            boolean matchBonus = lotto.isMatchBonusBall(winningLotto);
            Rank rank = Rank.of(count, matchBonus);
            profit += rank.getPrice();
            ranks.add(rank);
        }
    }

    public int countRank(Rank rank) {
        return (int) ranks.stream()
                .filter(status -> status.equals(rank))
                .count();
    }

    public double calculateRateOfReturn() {
        return (profit - money) / (double) money * 100;
    }

    public List<Lotto> getLottos() {
        return lottos;
    }

    public int getCountOfCustom() {
        return lottos.size();
    }

    public int getCountOfAuto() {
        return money / LOTTO_PRICE - lottos.size();
    }
}
