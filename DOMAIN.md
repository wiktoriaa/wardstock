# WardStock — Domain Guide

WardStock is used by a **District Medical Store (DMS)** to resupply the health facilities in its district: hospital wards and rural health centres.

Every month, each facility submits a **stock report** for every product it handles. At the start of the next month, WardStock calculates a **Suggested Order Quantity (SOQ)** for each product. The DMS store manager reviews the suggestions and dispatches the delivery. Deliveries happen **once a month**.

## Why the numbers matter

- **Ordering too little** means a stock-out: patients go without medicine until the next monthly delivery.
- **Ordering too much** is also a problem. Storage at facilities is small (some products need the cold chain), surplus stock expires, and the DMS stock is shared: what one facility over-orders, another facility does not get.

The goal of the calculation is to give each facility enough to reach its maximum stock level — no more, no less.

## Stock report

One report per facility, per product, per period.

| Field | Meaning |
|---|---|
| `opening` | Stock on hand at the start of the period |
| `received` | Quantity delivered during the period |
| `consumed` | Quantity issued to patients during the period |
| `losses` | Quantity lost: expired, damaged, stolen |
| `closing` | Stock on hand at the end of the period |
| `stockOutDays` | Number of days in the period with zero stock on hand |

A report is valid when `closing = opening + received − consumed − losses` and `stockOutDays` is between 0 and 30.

## Glossary and rules

**Reporting period** — a calendar month. **For all calculations, WardStock treats every period as 30 days**, whatever the length of the calendar month.

**Adjusted consumption (AC)** — an estimate of how much the facility *would have* consumed if the product had been available for the whole period:

```
AC = consumed × 30 / (30 − stockOutDays)
```

With no stock-out days, AC equals `consumed`.

**Unreliable period** — a period with **more than 20 stock-out days**. So little was available that its consumption says almost nothing about real demand. Unreliable periods are left out of the AMC.

**Average Monthly Consumption (AMC)** — the average of the adjusted consumption over the **three most recent periods**, up to and including the period being ordered for. Periods without a report and unreliable periods are left out; the average is taken over the periods that remain. AMC is rounded to the nearest whole unit (halves round up).
If no period remains, AMC cannot be calculated: the order line is marked **MANUAL REVIEW**, the SOQ is 0, and the store manager decides.

**Maximum stock months** — how many months of stock a facility should hold after a delivery:

| Facility type | Max stock months |
|---|---|
| Hospital (ward) | 3 |
| Health centre | 2 |

**Maximum stock** — `AMC × max stock months`.

**Suggested Order Quantity (SOQ)** — `maximum stock − closing`, never below 0, **rounded up to whole packs** (the DMS does not split packs).

**No report** — if a facility has not reported for the period being ordered for, no order is suggested. The line shows **NO REPORT** and the DMS follows up with the facility.

## Worked example

Health centre, Paracetamol 500 mg tablets, pack of 100. No stock-out days in the last three periods.

| Period | consumed | stockOutDays |
|---|---|---|
| January | 120 | 0 |
| February | 150 | 0 |
| March | 135 | 0 |

- AMC = (120 + 150 + 135) / 3 = **135**
- Maximum stock = 135 × 2 = **270**
- March closing balance = 80
- 270 − 80 = 190 → rounded up to whole packs → **SOQ = 200** (2 packs)
