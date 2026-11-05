package com.wardstock;

import java.time.YearMonth;

public record StockReport(
        String facilityCode,
        String productCode,
        YearMonth period,
        long opening,
        long received,
        long consumed,
        long losses,
        long closing,
        int stockOutDays) {
}
