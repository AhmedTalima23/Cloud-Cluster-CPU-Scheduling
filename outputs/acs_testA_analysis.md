#### testA (ACS, Q = 2, CS = 1)

| Process | Arrival | Burst | Priority |
|---|---|---|---|
| P1 | 0 | 9 | 3 |
| P2 | 1 | 2 | 2 |
| P3 | 2 | 1 | 1 |
| P4 | 4 | 6 | 2 |

| Time | CPU | Tier 1 | Tier 2 |
|---|---|---|---|
| 0 | P1 | [P1 (remaining: 9, Priority: 3)] | [Empty] |
| 1 | P1 | [P2 (remaining: 2, Priority: 2)] | [Empty] |
| 2 | CS: P1 -> P2 | [P2 (remaining: 2, Priority: 2), P3 (remaining: 1, Priority: 1)] | [P1 (remaining: 7, Priority: 3)] |
| 3 | P2 | [P3 (remaining: 1, Priority: 1)] | [P1 (remaining: 7, Priority: 3)] |
| 4 | P2 | [P3 (remaining: 1, Priority: 1), P4 (remaining: 6, Priority: 2)] | [P1 (remaining: 7, Priority: 3)] |
| 5 | CS: P2 -> P3 | [P3 (remaining: 1, Priority: 1), P4 (remaining: 6, Priority: 2)] | [P1 (remaining: 7, Priority: 3)] |
| 6 | P3 | [P4 (remaining: 6, Priority: 2)] | [P1 (remaining: 7, Priority: 2)] |
| 7 | CS: P3 -> P4 | [P4 (remaining: 6, Priority: 2)] | [P1 (remaining: 7, Priority: 2)] |
| 8 | P4 | [Empty] | [P1 (remaining: 7, Priority: 2)] |
| 10 | CS: P4 -> P1 | [Empty] | [P1 (remaining: 7, Priority: 1), P4 (remaining: 4, Priority: 2)] |
| 11 | P1 | [Empty] | [P4 (remaining: 4, Priority: 2)] |
| 18 | CS: P1 -> P4 | [Empty] | [P4 (remaining: 4, Priority: 0)] |
| 19 | P4 | [Empty] | [Empty] |
| 23 | DONE | [Empty] | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 9 | 18 | 18 | 9 | 0 |
| P2 | 1 | 2 | 5 | 4 | 2 | 2 |
| P3 | 2 | 1 | 7 | 5 | 4 | 4 |
| P4 | 4 | 6 | 23 | 19 | 13 | 4 |

Average turnaround time = 11.50, average waiting time = 7.00, average response time = 2.50, CPU utilization = 78.3% (18/23), context switches = 5

