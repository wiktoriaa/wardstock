# WardStock — order calculation module

Calculates the monthly Suggested Order Quantity for each facility and product from the facilities' stock reports.
See [DOMAIN.md](DOMAIN.md) for the business rules.

## Requirements

- Java 17+
- Maven 3.8+

## Build and test

```sh
mvn test
```

## Run

Prints the order sheet for one facility and one period:

```sh
mvn -q compile exec:java -Dexec.args="KSG-MAT 2026-03"
```

Facilities in the sample data: `KSG-MAT`, `KSG-PAED`, `MZM-HC`, `CHT-HC`. Reports cover January–March 2026.

## Layout

| Class | Role |
|---|---|
| `Main` | Command-line entry point, prints the order sheet |
| `CsvLoader` | Loads facilities, products and stock reports from `src/main/resources/data` |
| `ReportValidator` | Checks a stock report is internally consistent |
| `ConsumptionCalculator` | Average Monthly Consumption |
| `OrderCalculator` | Maximum stock and Suggested Order Quantity |
