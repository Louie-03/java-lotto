package domain;

import java.util.List;

public class LottosResult {
    private List<String> message;
    private double totalRateOfReturn;

    public LottosResult(List<String> message, double totalRateOfReturn) {
        this.message = message;
        this.totalRateOfReturn = totalRateOfReturn;
    }

    public List<String> getMessage() {
        return message;
    }

    public double getTotalRateOfReturn() {
        return totalRateOfReturn;
    }
}
