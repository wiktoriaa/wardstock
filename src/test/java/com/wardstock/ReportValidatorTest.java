package com.wardstock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.YearMonth;
import org.junit.jupiter.api.Test;

class ReportValidatorTest {

    private final ReportValidator validator = new ReportValidator();

    @Test
    void acceptsBalancedReport() {
        StockReport report = new StockReport("F", "P", YearMonth.parse("2026-03"), 130, 100, 135, 15, 80, 0);

        assertTrue(validator.validate(report).isEmpty());
    }

    @Test
    void rejectsUnbalancedClosing() {
        StockReport report = new StockReport("F", "P", YearMonth.parse("2026-03"), 130, 100, 135, 0, 80, 0);

        assertEquals(1, validator.validate(report).size());
    }

    @Test
    void rejectsStockOutDaysOutOfRange() {
        StockReport report = new StockReport("F", "P", YearMonth.parse("2026-03"), 0, 0, 0, 0, 0, 31);

        assertEquals(1, validator.validate(report).size());
    }
}
