package com.wardstock;

import java.util.ArrayList;
import java.util.List;

public class ReportValidator {

    static final int DAYS_IN_PERIOD = 30;

    public List<String> validate(StockReport report) {
        List<String> errors = new ArrayList<>();
        long expectedClosing = report.opening() + report.received() - report.consumed() - report.losses();
        if (report.closing() != expectedClosing) {
            errors.add("closing balance " + report.closing() + " does not match expected " + expectedClosing);
        }
        if (report.stockOutDays() < 0 || report.stockOutDays() > DAYS_IN_PERIOD) {
            errors.add("stock-out days must be between 0 and " + DAYS_IN_PERIOD);
        }
        if (report.consumed() < 0 || report.losses() < 0 || report.received() < 0) {
            errors.add("quantities must not be negative");
        }
        return errors;
    }
}
