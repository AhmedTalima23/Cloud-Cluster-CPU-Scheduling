#### sjf_test1 (ACS, Q = 3, CS = 1)

| Process | Arrival | Burst | Priority |
|---|---|---|---|
| P1 | 0 | 8 | 2 |
| P2 | 1 | 4 | 1 |
| P3 | 2 | 9 | 3 |
| P4 | 3 | 5 | 2 |

| Time | CPU | Tier 1 | Tier 2 |
|---|---|---|---|
| 0 | P1 | [P1 (remaining: 8, Priority: 2)] | [Empty] |
| 1 | P1 | [P2 (remaining: 4, Priority: 1)] | [Empty] |
| 2 | P1 | [P2 (remaining: 4, Priority: 1), P3 (remaining: 9, Priority: 3)] | [Empty] |
| 3 | CS: P1 -> P2 | [P2 (remaining: 4, Priority: 1), P3 (remaining: 9, Priority: 3), P4 (remaining: 5, Priority: 2)] | [P1 (remaining: 5, Priority: 2)] |
| 4 | P2 | [P3 (remaining: 9, Priority: 3), P4 (remaining: 5, Priority: 2)] | [P1 (remaining: 5, Priority: 2)] |
| 7 | CS: P2 -> P3 | [P3 (remaining: 9, Priority: 3), P4 (remaining: 5, Priority: 2)] | [P1 (remaining: 5, Priority: 1), P2 (remaining: 1, Priority: 1)] |
| 8 | P3 | [P4 (remaining: 5, Priority: 2)] | [P1 (remaining: 5, Priority: 1), P2 (remaining: 1, Priority: 1)] |
| 11 | CS: P3 -> P4 | [P4 (remaining: 5, Priority: 2)] | [P1 (remaining: 5, Priority: 0), P2 (remaining: 1, Priority: 0), P3 (remaining: 6, Priority: 3)] |
| 12 | P4 | [Empty] | [P1 (remaining: 5, Priority: 0), P2 (remaining: 1, Priority: 0), P3 (remaining: 6, Priority: 3)] |
| 15 | CS: P4 -> P1 | [Empty] | [P1 (remaining: 5, Priority: -1), P2 (remaining: 1, Priority: -1), P3 (remaining: 6, Priority: 2), P4 (remaining: 2, Priority: 2)] |
| 16 | P1 | [Empty] | [P2 (remaining: 1, Priority: -1), P3 (remaining: 6, Priority: 2), P4 (remaining: 2, Priority: 2)] |
| 21 | CS: P1 -> P2 | [Empty] | [P2 (remaining: 1, Priority: -2), P3 (remaining: 6, Priority: 1), P4 (remaining: 2, Priority: 1)] |
| 22 | P2 | [Empty] | [P3 (remaining: 6, Priority: 1), P4 (remaining: 2, Priority: 1)] |
| 23 | CS: P2 -> P3 | [Empty] | [P3 (remaining: 6, Priority: 0), P4 (remaining: 2, Priority: 0)] |
| 24 | P3 | [Empty] | [P4 (remaining: 2, Priority: 0)] |
| 30 | CS: P3 -> P4 | [Empty] | [P4 (remaining: 2, Priority: -1)] |
| 31 | P4 | [Empty] | [Empty] |
| 33 | DONE | [Empty] | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 8 | 21 | 21 | 13 | 0 |
| P2 | 1 | 4 | 23 | 22 | 18 | 3 |
| P3 | 2 | 9 | 30 | 28 | 19 | 6 |
| P4 | 3 | 5 | 33 | 30 | 25 | 9 |

Average turnaround time = 25.25, average waiting time = 18.75, average response time = 4.50, CPU utilization = 78.8% (26/33), context switches = 7

