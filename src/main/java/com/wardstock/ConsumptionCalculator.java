package com.wardstock;

import java.time.YearMonth;
import java.util.List;
import java.util.OptionalLong;

public class ConsumptionCalculator {

    static final int AMC_PERIODS = 3;
    static final int DAYS_IN_PERIOD = 30;
    static final int UNRELIABLE_STOCK_OUT_DAYS = 20; // more than this => period ignored

    public OptionalLong averageMonthlyConsumption(List<StockReport> history, YearMonth period) {
        YearMonth from = period.minusMonths(AMC_PERIODS - 1);
        List<Double> adjusted = history.stream()
                .filter(r -> !r.period().isBefore(from) && !r.period().isAfter(period))
                .filter(r -> r.stockOutDays() <= UNRELIABLE_STOCK_OUT_DAYS)
                .map(ConsumptionCalculator::adjustedConsumption)
                .toList();
        if (adjusted.isEmpty()) {
            return OptionalLong.empty();
        }
        double average = adjusted.stream().mapToDouble(Double::doubleValue).average().orElseThrow();
        return OptionalLong.of(Math.round(average));
    }

    static double adjustedConsumption(StockReport r) {
        return r.consumed() * (double) DAYS_IN_PERIOD / (DAYS_IN_PERIOD - r.stockOutDays());
    }
}
