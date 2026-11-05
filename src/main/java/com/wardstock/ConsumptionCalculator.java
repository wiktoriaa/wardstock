package com.wardstock;

import java.time.YearMonth;
import java.util.List;
import java.util.OptionalLong;

public class ConsumptionCalculator {

    static final int AMC_PERIODS = 3;

    /**
     * Average Monthly Consumption over the last {@value #AMC_PERIODS} periods,
     * up to and including {@code period}. Periods without a report are skipped.
     *
     * @return empty when there is no report in the window
     */
    public OptionalLong averageMonthlyConsumption(List<StockReport> history, YearMonth period) {
        YearMonth from = period.minusMonths(AMC_PERIODS - 1);
        List<StockReport> window = history.stream()
                .filter(r -> !r.period().isBefore(from) && !r.period().isAfter(period))
                .toList();
        if (window.isEmpty()) {
            return OptionalLong.empty();
        }
        double average = window.stream()
                .mapToLong(StockReport::consumed)
                .average()
                .orElseThrow();
        return OptionalLong.of(Math.round(average));
    }
}
