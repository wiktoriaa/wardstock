package com.wardstock;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: <facility-code> <period, e.g. 2026-03>");
            System.exit(1);
        }
        String facilityCode = args[0];
        YearMonth period = YearMonth.parse(args[1]);

        CsvLoader loader = new CsvLoader();
        Facility facility = loader.facilities().stream()
                .filter(f -> f.code().equals(facilityCode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown facility: " + facilityCode));
        Map<String, Product> products = loader.products().stream()
                .collect(Collectors.toMap(Product::code, Function.identity()));
        Map<String, List<StockReport>> historyByProduct = loader.reports().stream()
                .filter(r -> r.facilityCode().equals(facilityCode))
                .collect(Collectors.groupingBy(StockReport::productCode));

        OrderCalculator calculator = new OrderCalculator(new ConsumptionCalculator(), new ReportValidator());

        System.out.printf("Order sheet: %s (%s), after period %s%n%n", facility.name(), facility.type(), period);
        System.out.printf("%-8s %-45s %6s %6s %8s %8s  %s%n", "Product", "Name", "AMC", "Max", "Closing", "SOQ", "Status");
        historyByProduct.keySet().stream().sorted().forEach(code -> {
            OrderLine line = calculator.suggestOrder(facility, products.get(code), historyByProduct.get(code), period);
            System.out.printf("%-8s %-45s %6s %6s %8s %8d  %s%n",
                    code,
                    line.product().name(),
                    orDash(line.amc()),
                    orDash(line.maxStock()),
                    orDash(line.closing()),
                    line.suggestedQuantity(),
                    line.status());
        });
    }

    private static String orDash(Long value) {
        return value == null ? "-" : value.toString();
    }
}
