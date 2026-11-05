Subject: WS-27: fixed, ready before the 10th

Hi Grace,

Fixed and ready for the 10th. Two bugs, not a rule problem:

- The AMC ignored stock-out days, so the two weeks KSG-MAT was out of Oxytocin counted as low demand and dragged the average down.
- Suggested quantities were rounded *down* to whole packs instead of up, making every order up to one pack short.

Both now follow the guide: consumption is adjusted for stock-out days, months with more than 20 stock-out days are dropped from the AMC, and quantities round up. No 20% buffer - with these fixed it isn't needed, and it would have over-ordered everywhere else.

KSG-MAT Oxytocin 10 IU: **144 → 174**. Could you check with the ward in-charge that this looks right?

Two things to know before the 10th:

- Expect higher numbers wherever there were stock-outs, and sometimes one pack more elsewhere from the rounding.
- A few more lines may come up as MANUAL REVIEW, where every month was missing or badly stocked out. Those are the products that have been running short, so worth a look.

I assumed the 3-month window stays, one report per product per month, missing months skipped, and that exactly 20 stock-out days still counts (21 doesn't). Shout if any of that is off.

Best,
Wiktoria