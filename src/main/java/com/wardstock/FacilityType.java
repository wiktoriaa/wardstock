package com.wardstock;

public enum FacilityType {
    HOSPITAL(3),
    HEALTH_CENTRE(2);

    private final int maxStockMonths;

    FacilityType(int maxStockMonths) {
        this.maxStockMonths = maxStockMonths;
    }

    public int maxStockMonths() {
        return maxStockMonths;
    }
}
