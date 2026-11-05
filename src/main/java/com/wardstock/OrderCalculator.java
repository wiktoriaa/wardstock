package com.wardstock;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class OrderCalculator {

    private final ConsumptionCalculator consumptionCalculator;
    private final ReportValidator reportValidator;

    public OrderCalculator(ConsumptionCalculator consumptionCalculator, ReportValidator reportValidator) {
        this.consumptionCalculator = consumptionCalculator;
        this.reportValidator = reportValidator;
    }

    /**
     * Suggested order for one product at one facility, for the delivery that follows {@code period}.
     *
     * @param history all stock reports of this product at this facility
     */
    public OrderLine suggestOrder(Facility facility, Product product, List<StockReport> history, YearMonth period) {
        Optional<StockReport> current = history.stream()
                .filter(r -> r.period().equals(period))
                .findFirst();
        if (current.isEmpty()) {
            return new OrderLine(product, null, null, null, 0, OrderLine.Status.NO_REPORT);
        }
        StockReport report = current.get();
        if (!reportValidator.validate(report).isEmpty()) {
            return new OrderLine(product, null, null, report.closing(), 0, OrderLine.Status.INVALID_REPORT);
        }

        OptionalLong amc = consumptionCalculator.averageMonthlyConsumption(history, period);
        if (amc.isEmpty()) {
            return new OrderLine(product, null, null, report.closing(), 0, OrderLine.Status.MANUAL_REVIEW);
        }

        long maxStock = amc.getAsLong() * facility.type().maxStockMonths();
        long needed = Math.max(0, maxStock - report.closing());
        long suggested = roundToPacks(needed, product.packSize());
        return new OrderLine(product, amc.getAsLong(), maxStock, report.closing(), suggested, OrderLine.Status.OK);
    }

    static long roundToPacks(long quantity, int packSize) {
        return (quantity / packSize) * packSize;
    }
}
