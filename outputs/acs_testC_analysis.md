#### testC (ACS, Q = 3, CS = 1)

| Process | Arrival | Burst | Priority |
|---|---|---|---|
| P1 | 0 | 7 | 2 |
| P2 | 3 | 3 | 1 |
| P3 | 6 | 2 | 3 |

| Time | CPU | Tier 1 | Tier 2 |
|---|---|---|---|
| 0 | P1 | [P1 (remaining: 7, Priority: 2)] | [Empty] |
| 3 | CS: P1 -> P2 | [P2 (remaining: 3, Priority: 1)] | [P1 (remaining: 4, Priority: 2)] |
| 4 | P2 | [Empty] | [P1 (remaining: 4, Priority: 2)] |
| 6 | P2 | [P3 (remaining: 2, Priority: 3)] | [P1 (remaining: 4, Priority: 2)] |
| 7 | CS: P2 -> P3 | [P3 (remaining: 2, Priority: 3)] | [P1 (remaining: 4, Priority: 1)] |
| 8 | P3 | [Empty] | [P1 (remaining: 4, Priority: 1)] |
| 10 | CS: P3 -> P1 | [Empty] | [P1 (remaining: 4, Priority: 1)] |
| 11 | P1 | [Empty] | [Empty] |
| 15 | DONE | [Empty] | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 7 | 15 | 15 | 8 | 0 |
| P2 | 3 | 3 | 7 | 4 | 1 | 1 |
| P3 | 6 | 2 | 10 | 4 | 2 | 2 |

Average turnaround time = 7.67, average waiting time = 3.67, average response time = 1.00, CPU utilization = 80.0% (12/15), context switches = 3

