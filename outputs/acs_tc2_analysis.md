#### tc2 (ACS, Q = 3, CS = 1)

| Process | Arrival | Burst | Priority |
|---|---|---|---|
| P1 | 0 | 5 | 4 |
| P2 | 1 | 6 | 2 |
| P3 | 4 | 4 | 3 |

| Time | CPU | Tier 1 | Tier 2 |
|---|---|---|---|
| 0 | P1 | [P1 (remaining: 5, Priority: 4)] | [Empty] |
| 1 | P1 | [P2 (remaining: 6, Priority: 2)] | [Empty] |
| 3 | CS: P1 -> P2 | [P2 (remaining: 6, Priority: 2)] | [P1 (remaining: 2, Priority: 4)] |
| 4 | P2 | [P3 (remaining: 4, Priority: 3)] | [P1 (remaining: 2, Priority: 4)] |
| 7 | CS: P2 -> P3 | [P3 (remaining: 4, Priority: 3)] | [P1 (remaining: 2, Priority: 3), P2 (remaining: 3, Priority: 2)] |
| 8 | P3 | [Empty] | [P1 (remaining: 2, Priority: 3), P2 (remaining: 3, Priority: 2)] |
| 11 | CS: P3 -> P2 | [Empty] | [P1 (remaining: 2, Priority: 2), P2 (remaining: 3, Priority: 1), P3 (remaining: 1, Priority: 3)] |
| 12 | P2 | [Empty] | [P1 (remaining: 2, Priority: 2), P3 (remaining: 1, Priority: 3)] |
| 15 | CS: P2 -> P1 | [Empty] | [P1 (remaining: 2, Priority: 1), P3 (remaining: 1, Priority: 2)] |
| 16 | P1 | [Empty] | [P3 (remaining: 1, Priority: 2)] |
| 18 | CS: P1 -> P3 | [Empty] | [P3 (remaining: 1, Priority: 2)] |
| 19 | P3 | [Empty] | [Empty] |
| 20 | DONE | [Empty] | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 5 | 18 | 18 | 13 | 0 |
| P2 | 1 | 6 | 15 | 14 | 8 | 3 |
| P3 | 4 | 4 | 20 | 16 | 12 | 4 |

Average turnaround time = 16.00, average waiting time = 11.00, average response time = 2.33, CPU utilization = 75.0% (15/20), context switches = 5

