#### testB (ACS, Q = 2, CS = 0)

| Process | Arrival | Burst | Priority |
|---|---|---|---|
| P1 | 0 | 4 | 6 |
| P2 | 0 | 5 | 5 |
| P3 | 1 | 5 | 5 |
| P4 | 2 | 5 | 5 |
| P5 | 3 | 5 | 5 |

| Time | CPU | Tier 1 | Tier 2 |
|---|---|---|---|
| 0 | P1 | [P1 (remaining: 4, Priority: 6), P2 (remaining: 5, Priority: 5)] | [Empty] |
| 1 | P1 | [P2 (remaining: 5, Priority: 5), P3 (remaining: 5, Priority: 5)] | [Empty] |
| 2 | P2 | [P2 (remaining: 5, Priority: 5), P3 (remaining: 5, Priority: 5), P4 (remaining: 5, Priority: 5)] | [P1 (remaining: 2, Priority: 6)] |
| 3 | P2 | [P3 (remaining: 5, Priority: 5), P4 (remaining: 5, Priority: 5), P5 (remaining: 5, Priority: 5)] | [P1 (remaining: 2, Priority: 6)] |
| 4 | P3 | [P3 (remaining: 5, Priority: 5), P4 (remaining: 5, Priority: 5), P5 (remaining: 5, Priority: 5)] | [P1 (remaining: 2, Priority: 6), P2 (remaining: 3, Priority: 5)] |
| 6 | P4 | [P4 (remaining: 5, Priority: 5), P5 (remaining: 5, Priority: 5)] | [P1 (remaining: 2, Priority: 5), P2 (remaining: 3, Priority: 5), P3 (remaining: 3, Priority: 5)] |
| 8 | P5 | [P5 (remaining: 5, Priority: 5)] | [P1 (remaining: 2, Priority: 5), P2 (remaining: 3, Priority: 4), P3 (remaining: 3, Priority: 5), P4 (remaining: 3, Priority: 5)] |
| 10 | P1 | [Empty] | [P1 (remaining: 2, Priority: 4), P2 (remaining: 3, Priority: 4), P3 (remaining: 3, Priority: 4), P4 (remaining: 3, Priority: 5), P5 (remaining: 3, Priority: 5)] |
| 12 | P2 | [Empty] | [P2 (remaining: 3, Priority: 3), P3 (remaining: 3, Priority: 4), P4 (remaining: 3, Priority: 4), P5 (remaining: 3, Priority: 5)] |
| 15 | P3 | [Empty] | [P3 (remaining: 3, Priority: 3), P4 (remaining: 3, Priority: 4), P5 (remaining: 3, Priority: 4)] |
| 18 | P4 | [Empty] | [P4 (remaining: 3, Priority: 3), P5 (remaining: 3, Priority: 3)] |
| 21 | P5 | [Empty] | [P5 (remaining: 3, Priority: 3)] |
| 24 | DONE | [Empty] | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 4 | 12 | 12 | 8 | 0 |
| P2 | 0 | 5 | 15 | 15 | 10 | 2 |
| P3 | 1 | 5 | 18 | 17 | 12 | 3 |
| P4 | 2 | 5 | 21 | 19 | 14 | 4 |
| P5 | 3 | 5 | 24 | 21 | 16 | 5 |

Average turnaround time = 16.80, average waiting time = 12.00, average response time = 2.80, CPU utilization = 100.0% (24/24), context switches = 9

