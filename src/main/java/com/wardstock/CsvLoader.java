package com.wardstock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.util.List;

public class CsvLoader {

    private static final String DATA_DIR = "/data/";

    public List<Facility> facilities() {
        return rows("facilities.csv").stream()
                .map(c -> new Facility(c[0], c[1], FacilityType.valueOf(c[2])))
                .toList();
    }

    public List<Product> products() {
        return rows("products.csv").stream()
                .map(c -> new Product(c[0], c[1], Integer.parseInt(c[2])))
                .toList();
    }

    public List<StockReport> reports() {
        return rows("reports.csv").stream()
                .map(c -> new StockReport(
                        c[0],
                        c[1],
                        YearMonth.parse(c[2]),
                        Long.parseLong(c[3]),
                        Long.parseLong(c[4]),
                        Long.parseLong(c[5]),
                        Long.parseLong(c[6]),
                        Long.parseLong(c[7]),
                        Integer.parseInt(c[8])))
                .toList();
    }

    private List<String[]> rows(String file) {
        InputStream in = CsvLoader.class.getResourceAsStream(DATA_DIR + file);
        if (in == null) {
            throw new IllegalStateException("Missing data file: " + file);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.lines()
                    .skip(1)
                    .filter(line -> !line.isBlank())
                    .map(line -> line.split(","))
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
