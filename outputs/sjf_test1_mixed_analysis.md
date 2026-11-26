#### sjf_test1_mixed (Preemptive SJF, CS = 1)

| Process | Arrival | Burst |
|---|---|---|
| P1 | 0 | 8 |
| P2 | 1 | 4 |
| P3 | 2 | 9 |
| P4 | 3 | 5 |

| Time | CPU | Ready queue |
|---|---|---|
| 0 | P1 | [Empty] |
| 1 | CS: P1 -> P2 | [P1 (remaining: 7)] |
| 2 | P2 | [P1 (remaining: 7), P3 (remaining: 9)] |
| 3-5 | P2 | [P4 (remaining: 5), P1 (remaining: 7), P3 (remaining: 9)] |
| 6 | CS: P2 -> P4 | [P1 (remaining: 7), P3 (remaining: 9)] |
| 7-11 | P4 | [P1 (remaining: 7), P3 (remaining: 9)] |
| 12 | CS: P4 -> P1 | [P3 (remaining: 9)] |
| 13-19 | P1 | [P3 (remaining: 9)] |
| 20 | CS: P1 -> P3 | [Empty] |
| 21-29 | P3 | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 8 | 20 | 20 | 12 | 0 |
| P2 | 1 | 4 | 6 | 5 | 1 | 1 |
| P3 | 2 | 9 | 30 | 28 | 19 | 19 |
| P4 | 3 | 5 | 12 | 9 | 4 | 4 |

Average turnaround time = 15.50, average waiting time = 9.00, average response time = 6.00, CPU utilization = 86.7% (26/30), context switches = 4

