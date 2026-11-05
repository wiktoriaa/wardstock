package com.wardstock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.Test;

class ConsumptionCalculatorTest {

    private final ConsumptionCalculator calculator = new ConsumptionCalculator();

    @Test
    void averagesLastThreePeriods() {
        List<StockReport> history = List.of(
                report("2026-01", 120, 0),
                report("2026-02", 150, 0),
                report("2026-03", 135, 0));

        assertEquals(135, calculator.averageMonthlyConsumption(history, YearMonth.parse("2026-03")).getAsLong());
    }

    @Test
    void ignoresPeriodsOutsideTheWindow() {
        List<StockReport> history = List.of(
                report("2025-12", 1000, 0),
                report("2026-01", 120, 0),
                report("2026-02", 150, 0),
                report("2026-03", 135, 0),
                report("2026-04", 1000, 0));

        assertEquals(135, calculator.averageMonthlyConsumption(history, YearMonth.parse("2026-03")).getAsLong());
    }

    @Test
    void skipsPeriodsWithoutReport() {
        List<StockReport> history = List.of(
                report("2026-01", 50, 0),
                report("2026-03", 46, 0));

        assertEquals(48, calculator.averageMonthlyConsumption(history, YearMonth.parse("2026-03")).getAsLong());
    }

    @Test
    void usesConsumptionAsReported() {
        List<StockReport> history = List.of(
                report("2026-01", 60, 0),
                report("2026-02", 54, 0),
                report("2026-03", 30, 15));

        assertEquals(48, calculator.averageMonthlyConsumption(history, YearMonth.parse("2026-03")).getAsLong());
    }

    @Test
    void emptyWhenNoReportsInWindow() {
        assertTrue(calculator.averageMonthlyConsumption(List.of(), YearMonth.parse("2026-03")).isEmpty());
    }

    private static StockReport report(String period, long consumed, int stockOutDays) {
        return new StockReport("F", "P", YearMonth.parse(period), 0, consumed, consumed, 0, 0, stockOutDays);
    }
}
