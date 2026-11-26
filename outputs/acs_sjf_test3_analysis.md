#### sjf_test3 (ACS, Q = 3, CS = 2)

| Process | Arrival | Burst | Priority |
|---|---|---|---|
| P1 | 0 | 3 | 1 |
| P2 | 0 | 3 | 1 |
| P3 | 0 | 2 | 1 |
| P4 | 15 | 4 | 1 |
| P5 | 15 | 1 | 1 |

| Time | CPU | Tier 1 | Tier 2 |
|---|---|---|---|
| 0 | P1 | [P1 (remaining: 3, Priority: 1), P2 (remaining: 3, Priority: 1), P3 (remaining: 2, Priority: 1)] | [Empty] |
| 3 | CS: P1 -> P2 | [P2 (remaining: 3, Priority: 1), P3 (remaining: 2, Priority: 1)] | [Empty] |
| 5 | P2 | [P3 (remaining: 2, Priority: 1)] | [Empty] |
| 8 | CS: P2 -> P3 | [P3 (remaining: 2, Priority: 1)] | [Empty] |
| 10 | P3 | [Empty] | [Empty] |
| 12 | IDLE | [Empty] | [Empty] |
| 15 | CS: P3 -> P4 | [P4 (remaining: 4, Priority: 1), P5 (remaining: 1, Priority: 1)] | [Empty] |
| 17 | P4 | [P5 (remaining: 1, Priority: 1)] | [Empty] |
| 20 | CS: P4 -> P5 | [P5 (remaining: 1, Priority: 1)] | [P4 (remaining: 1, Priority: 1)] |
| 22 | P5 | [Empty] | [P4 (remaining: 1, Priority: 1)] |
| 23 | CS: P5 -> P4 | [Empty] | [P4 (remaining: 1, Priority: 1)] |
| 25 | P4 | [Empty] | [Empty] |
| 26 | DONE | [Empty] | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 3 | 3 | 3 | 0 | 0 |
| P2 | 0 | 3 | 8 | 8 | 5 | 5 |
| P3 | 0 | 2 | 12 | 12 | 10 | 10 |
| P4 | 15 | 4 | 26 | 11 | 7 | 2 |
| P5 | 15 | 1 | 23 | 8 | 7 | 7 |

Average turnaround time = 8.40, average waiting time = 5.80, average response time = 4.80, CPU utilization = 50.0% (13/26), context switches = 5

