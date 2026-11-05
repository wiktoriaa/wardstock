package com.wardstock;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderCalculatorTest {

    private static final YearMonth MARCH = YearMonth.parse("2026-03");

    private final OrderCalculator calculator = new OrderCalculator(new ConsumptionCalculator(), new ReportValidator());

    private final Product units = new Product("P1", "Units", 1);
    private final Facility hospital = new Facility("H", "Hospital ward", FacilityType.HOSPITAL);
    private final Facility healthCentre = new Facility("HC", "Health centre", FacilityType.HEALTH_CENTRE);

    @Test
    void hospitalHoldsThreeMonths() {
        OrderLine line = calculator.suggestOrder(hospital, units, steadyHistory(100, 50), MARCH);

        assertEquals(300, line.maxStock());
        assertEquals(250, line.suggestedQuantity());
    }

    @Test
    void healthCentreHoldsTwoMonths() {
        OrderLine line = calculator.suggestOrder(healthCentre, units, steadyHistory(100, 50), MARCH);

        assertEquals(200, line.maxStock());
        assertEquals(150, line.suggestedQuantity());
    }

    @Test
    void neverSuggestsNegativeQuantity() {
        OrderLine line = calculator.suggestOrder(healthCentre, units, steadyHistory(100, 500), MARCH);

        assertEquals(0, line.suggestedQuantity());
    }

    @Test
    void noReportForPeriod() {
        OrderLine line = calculator.suggestOrder(hospital, units, List.of(), MARCH);

        assertEquals(OrderLine.Status.NO_REPORT, line.status());
        assertEquals(0, line.suggestedQuantity());
    }

    @Test
    void invalidReportIsNotOrdered() {
        StockReport broken = new StockReport("H", "P1", MARCH, 10, 0, 5, 0, 99, 0);

        OrderLine line = calculator.suggestOrder(hospital, units, List.of(broken), MARCH);

        assertEquals(OrderLine.Status.INVALID_REPORT, line.status());
    }

    @Test
    void roundsToWholePacks() {
        assertEquals(200, OrderCalculator.roundToPacks(200, 100));
        assertEquals(30, OrderCalculator.roundToPacks(30, 10));
    }

    /** Three months of the same consumption, ending with the given closing balance in March. */
    private static List<StockReport> steadyHistory(long consumed, long marchClosing) {
        return List.of(
                new StockReport("F", "P1", YearMonth.parse("2026-01"), consumed, consumed, consumed, 0, consumed, 0),
                new StockReport("F", "P1", YearMonth.parse("2026-02"), consumed, consumed, consumed, 0, consumed, 0),
                new StockReport("F", "P1", MARCH, marchClosing, consumed, consumed, 0, marchClosing, 0));
    }
}
