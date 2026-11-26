#### sjf_test4 (ACS, Q = 3, CS = 1)

| Process | Arrival | Burst | Priority |
|---|---|---|---|
| P1 | 0 | 6 | 2 |
| P2 | 2 | 4 | 1 |
| P3 | 3 | 1 | 1 |
| P4 | 3 | 4 | 1 |

| Time | CPU | Tier 1 | Tier 2 |
|---|---|---|---|
| 0 | P1 | [P1 (remaining: 6, Priority: 2)] | [Empty] |
| 2 | P1 | [P2 (remaining: 4, Priority: 1)] | [Empty] |
| 3 | CS: P1 -> P2 | [P2 (remaining: 4, Priority: 1), P3 (remaining: 1, Priority: 1), P4 (remaining: 4, Priority: 1)] | [P1 (remaining: 3, Priority: 2)] |
| 4 | P2 | [P3 (remaining: 1, Priority: 1), P4 (remaining: 4, Priority: 1)] | [P1 (remaining: 3, Priority: 2)] |
| 7 | CS: P2 -> P3 | [P3 (remaining: 1, Priority: 1), P4 (remaining: 4, Priority: 1)] | [P1 (remaining: 3, Priority: 1), P2 (remaining: 1, Priority: 1)] |
| 8 | P3 | [P4 (remaining: 4, Priority: 1)] | [P1 (remaining: 3, Priority: 1), P2 (remaining: 1, Priority: 1)] |
| 9 | CS: P3 -> P4 | [P4 (remaining: 4, Priority: 1)] | [P1 (remaining: 3, Priority: 1), P2 (remaining: 1, Priority: 1)] |
| 10 | P4 | [Empty] | [P1 (remaining: 3, Priority: 1), P2 (remaining: 1, Priority: 1)] |
| 13 | CS: P4 -> P1 | [Empty] | [P1 (remaining: 3, Priority: 0), P2 (remaining: 1, Priority: 0), P4 (remaining: 1, Priority: 1)] |
| 14 | P1 | [Empty] | [P2 (remaining: 1, Priority: 0), P4 (remaining: 1, Priority: 1)] |
| 17 | CS: P1 -> P2 | [Empty] | [P2 (remaining: 1, Priority: -1), P4 (remaining: 1, Priority: 0)] |
| 18 | P2 | [Empty] | [P4 (remaining: 1, Priority: 0)] |
| 19 | CS: P2 -> P4 | [Empty] | [P4 (remaining: 1, Priority: 0)] |
| 20 | P4 | [Empty] | [Empty] |
| 21 | DONE | [Empty] | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 6 | 17 | 17 | 11 | 0 |
| P2 | 2 | 4 | 19 | 17 | 13 | 2 |
| P3 | 3 | 1 | 9 | 6 | 5 | 5 |
| P4 | 3 | 4 | 21 | 18 | 14 | 7 |

Average turnaround time = 14.50, average waiting time = 10.75, average response time = 3.50, CPU utilization = 71.4% (15/21), context switches = 6

