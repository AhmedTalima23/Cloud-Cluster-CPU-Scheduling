#### tc1 (ACS, Q = 3, CS = 0)

| Process | Arrival | Burst | Priority |
|---|---|---|---|
| P1 | 0 | 4 | 2 |
| P2 | 1 | 7 | 3 |
| P3 | 3 | 2 | 1 |

| Time | CPU | Tier 1 | Tier 2 |
|---|---|---|---|
| 0 | P1 | [P1 (remaining: 4, Priority: 2)] | [Empty] |
| 1 | P1 | [P2 (remaining: 7, Priority: 3)] | [Empty] |
| 3 | P2 | [P2 (remaining: 7, Priority: 3), P3 (remaining: 2, Priority: 1)] | [P1 (remaining: 1, Priority: 2)] |
| 6 | P3 | [P3 (remaining: 2, Priority: 1)] | [P1 (remaining: 1, Priority: 2), P2 (remaining: 4, Priority: 3)] |
| 8 | P1 | [Empty] | [P1 (remaining: 1, Priority: 1), P2 (remaining: 4, Priority: 3)] |
| 9 | P2 | [Empty] | [P2 (remaining: 4, Priority: 3)] |
| 13 | DONE | [Empty] | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 4 | 9 | 9 | 5 | 0 |
| P2 | 1 | 7 | 13 | 12 | 5 | 2 |
| P3 | 3 | 2 | 8 | 5 | 3 | 3 |

Average turnaround time = 8.67, average waiting time = 4.33, average response time = 1.67, CPU utilization = 100.0% (13/13), context switches = 4

