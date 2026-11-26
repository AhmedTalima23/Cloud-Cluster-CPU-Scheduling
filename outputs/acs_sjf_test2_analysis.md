#### sjf_test2 (ACS, Q = 3, CS = 1)

| Process | Arrival | Burst | Priority |
|---|---|---|---|
| P1 | 0 | 12 | 3 |
| P2 | 1 | 2 | 1 |
| P3 | 4 | 2 | 1 |
| P4 | 7 | 2 | 1 |
| P5 | 10 | 2 | 1 |
| P6 | 13 | 3 | 1 |

| Time | CPU | Tier 1 | Tier 2 |
|---|---|---|---|
| 0 | P1 | [P1 (remaining: 12, Priority: 3)] | [Empty] |
| 1 | P1 | [P2 (remaining: 2, Priority: 1)] | [Empty] |
| 3 | CS: P1 -> P2 | [P2 (remaining: 2, Priority: 1)] | [P1 (remaining: 9, Priority: 3)] |
| 4 | P2 | [P3 (remaining: 2, Priority: 1)] | [P1 (remaining: 9, Priority: 3)] |
| 6 | CS: P2 -> P3 | [P3 (remaining: 2, Priority: 1)] | [P1 (remaining: 9, Priority: 3)] |
| 7 | P3 | [P4 (remaining: 2, Priority: 1)] | [P1 (remaining: 9, Priority: 2)] |
| 9 | CS: P3 -> P4 | [P4 (remaining: 2, Priority: 1)] | [P1 (remaining: 9, Priority: 2)] |
| 10 | P4 | [P5 (remaining: 2, Priority: 1)] | [P1 (remaining: 9, Priority: 2)] |
| 12 | CS: P4 -> P5 | [P5 (remaining: 2, Priority: 1)] | [P1 (remaining: 9, Priority: 1)] |
| 13 | P5 | [P6 (remaining: 3, Priority: 1)] | [P1 (remaining: 9, Priority: 1)] |
| 15 | CS: P5 -> P6 | [P6 (remaining: 3, Priority: 1)] | [P1 (remaining: 9, Priority: 0)] |
| 16 | P6 | [Empty] | [P1 (remaining: 9, Priority: 0)] |
| 19 | CS: P6 -> P1 | [Empty] | [P1 (remaining: 9, Priority: -1)] |
| 20 | P1 | [Empty] | [Empty] |
| 29 | DONE | [Empty] | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 12 | 29 | 29 | 17 | 0 |
| P2 | 1 | 2 | 6 | 5 | 3 | 3 |
| P3 | 4 | 2 | 9 | 5 | 3 | 3 |
| P4 | 7 | 2 | 12 | 5 | 3 | 3 |
| P5 | 10 | 2 | 15 | 5 | 3 | 3 |
| P6 | 13 | 3 | 19 | 6 | 3 | 3 |

Average turnaround time = 9.17, average waiting time = 5.33, average response time = 2.50, CPU utilization = 79.3% (23/29), context switches = 6

